package com.simu.service.impl;

import com.simu.constant.ResponseCodeEnum;
import com.simu.dao.IAuthDao;
import com.simu.dao.IBucketDao;
import com.simu.dao.IFileDao;
import com.simu.dao.IFolderDao;
import com.simu.exception.ErrorCodeException;
import com.simu.model.Bucket;
import com.simu.model.File;
import com.simu.model.Folder;
import com.simu.seaweedfs.core.FileSource;
import com.simu.seaweedfs.core.FileTemplate;
import com.simu.seaweedfs.core.file.FileHandleStatus;
import com.simu.service.IFileService;
import com.simu.utils.FileUtil;
import com.simu.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Service
public class FileService implements IFileService {

    @Autowired
    FileSource fileSource;

    @Autowired
    IBucketDao bucketDao;

    @Autowired
    IAuthDao authDao;

    @Autowired
    IFileDao fileDao;

    @Autowired
    IFolderDao folderDao;

    FileTemplate fileTemplate;

    @PostConstruct
    public void init() {
        this.fileTemplate = new FileTemplate(fileSource.getConnection());
    }

    @Override
    public ResponseEntity getFile(String path, String bucket, String accessId, long expires, String signature) throws Exception {
        //TODO 判断 bucket的公开性
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (null == buck) {
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        if (buck.getIsPublic() == 1) {
            // 公开

        }

        headers.setContentDispositionFormData("attachment", "abc.zip");
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) fileTemplate.getFileStream("2,02e1b65433").getOutputStream();
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }

    @Override
    public void putFile(CommonsMultipartFile file, String path, String bucket, String accessId, long expires, String signature) throws Exception {
        String resource = "/" + bucket + "/" + path;
        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.PUT);
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (null == buck) {
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        String fileName = FileUtil.getSimpleFileName(path);
        String purePath = FileUtil.getPurePath(path);
        File fileEntity = fileDao.getFileByPath(buck.getId(), purePath, fileName);
        if (null != fileEntity) {
            // 删除原文件重新上传
            fileTemplate.deleteFile(fileEntity.getNumber());
            FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream(fileName, file.getInputStream());
            fileEntity.setNumber(fileHandleStatus.getFileId());
            fileEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
            fileEntity.setSize(fileHandleStatus.getSize());
            fileEntity.update();
        } else {
            FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream(fileName, file.getInputStream());
            fileEntity = new File(fileName, fileHandleStatus.getFileId(), purePath, fileHandleStatus.getSize(), buck.getId());

        }
    }

    public long createFolders(String purePath, long bucketId) {
        String[] folders = purePath.split("/");
        String parentPath = "";
        long parentId = 0;
        for (int i = 0; i < folders.length; i++) {
            Folder folder = folderDao.getFolderByPath(bucketId, parentPath, folders[i]);
            if (null == folder) {
                folder = new Folder(folders[i], parentPath, parentId, bucketId);
                folder.save();
            }
            parentId = folder.getId();
            if ("".equals(parentPath)){
                parentPath = folder.getName();
            }else{
                parentPath += "/" + folder.getName();
            }
        }
        return parentId;
    }
}
