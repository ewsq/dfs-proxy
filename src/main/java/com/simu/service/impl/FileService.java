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
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity getFile(String path, String bucket, String accessId, Long expires, String signature) throws Exception {
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (null == buck) {
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        String fileName = FileUtil.getSimpleFileName(path);
        String purePath = FileUtil.getPurePath(path);
        File fileEntity = fileDao.getFileByPath(buck.getId(), purePath, fileName);
        if (null == fileEntity){
            throw new ErrorCodeException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        if (buck.getIsPublic() == 0) {
            // 私密
            String resource = "/" + bucket + "/" + path;
            authDao.validSignature(resource, accessId, expires, signature, RequestMethod.GET);
        }
        headers.setContentDispositionFormData("attachment", fileEntity.getName());
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) fileTemplate.getFileStream(fileEntity.getNumber()).getOutputStream();
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }

    @Override
    public void putFile(MultipartFile file, String path, String bucket, String accessId, Long expires, String signature) throws Exception {
//        String resource = "/" + bucket + "/" + path;
//        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.POST);
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
            //TODO assign key 单点故障;
            // 运行时有master宕机的情况(1. peer挂 2.leader挂) 主动发现和被动发现
            FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream(fileName, file.getInputStream(), bucket);
            fileEntity = new File(fileName, fileHandleStatus.getFileId(), purePath, fileHandleStatus.getSize(), buck.getId());
            fileEntity.setFolderId(createFolders(purePath, buck.getId()));
            fileEntity.save();
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
