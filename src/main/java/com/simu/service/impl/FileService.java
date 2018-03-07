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
import com.simu.vo.SimpleFileVO;
import com.simu.vo.SimpleFolderVO;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Service
public class FileService implements IFileService {

    @Autowired
    IBucketDao bucketDao;

    @Autowired
    IAuthDao authDao;

    @Autowired
    IFileDao fileDao;

    @Autowired
    IFolderDao folderDao;

    @Autowired
    FileTemplate fileTemplate;

    @Override
    public ResponseEntity getFile(String path, String bucket, String accessId, Long expires, String signature) throws Exception {
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (buck.getIsPublic() == 0) {
            // 私密
            String resource = "/" + bucket + "/" + path;
            authDao.validSignature(resource, accessId, expires, signature, RequestMethod.GET);
        }
        return getFile(path, bucket);
    }

    @Override
    public ResponseEntity getFile(String path, String bucket) throws Exception {
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (null == buck) {
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        String fileName = FileUtil.getSimpleFileName(path);
        String purePath = FileUtil.getPurePath(path);
        File fileEntity = fileDao.getFileByPath(buck.getId(), purePath, fileName);
        if (null == fileEntity) {
            throw new ErrorCodeException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String downloadFileName = new String(fileEntity.getName().getBytes("UTF-8"),"iso-8859-1");
        headers.setContentDispositionFormData("attachment", downloadFileName);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) fileTemplate.getFileStream(fileEntity.getNumber()).getOutputStream();
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }

    @Override
    public void putFile(MultipartFile file, String path, String bucket, String accessId, Long expires, String signature) throws Exception {
        String resource = "/" + bucket + "/" + path;
        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.POST);
        putFile(file, path, bucket);
    }

    @Override
    public void putFile(MultipartFile file, String path, String bucket) throws Exception {
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
            // assign key 单点故障;
            // 运行时有master宕机的情况(1. peer挂 2.leader挂) 主动发现和被动发现
            FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream(fileName, file.getInputStream(), bucket);
            fileEntity = new File(fileName, fileHandleStatus.getFileId(), purePath, fileHandleStatus.getSize(), buck.getId());
            fileEntity.setFolderId(createFolders(purePath, buck.getId()));
            fileEntity.save();
        }
    }

    public long createFolders(String purePath, long bucketId) {
        purePath = purePath.replace(" ", "");
        if ("".equals(purePath)) {
            throw new ErrorCodeException(ResponseCodeEnum.BAD_REQUEST_PARAM.getCode(), ResponseCodeEnum.BAD_REQUEST_PARAM.getMsg() + ":path");
        }
        if (bucketDao.findById(bucketId) == null) {
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        String[] folders = purePath.split("/");
        String parentPath = "";
        long parentId = 0;
        for (int i = 0; i < folders.length; i++) {
            if ("".equals(folders[i])) {
                throw new ErrorCodeException(ResponseCodeEnum.BAD_REQUEST_PARAM.getCode(), ResponseCodeEnum.BAD_REQUEST_PARAM.getMsg() + ":path");
            }
            Folder folder = folderDao.getFolderByPath(bucketId, parentPath, folders[i]);
            if (null == folder) {
                folder = new Folder(folders[i], parentPath, parentId, bucketId);
                folder.save();
            }
            parentId = folder.getId();
            if ("".equals(parentPath)) {
                parentPath = folder.getName();
            } else {
                parentPath += "/" + folder.getName();
            }
        }
        return parentId;
    }

    @Override
    public void deleteFilesByBucketId(long bucketId) {
        List<File> files = fileDao.getFilesByBucketId(bucketId);
        files.stream().forEach(file -> {
            safeDeleteFile(file.getNumber());
            fileDao.deleteFile(file.getId());
        });
    }

    @Override
    public Map<String, List> getFileAndFoldersByPath(long bucketId, String path, String keyword) {
        List<SimpleFolderVO> simpleFolderVOS = new ArrayList<>();
        List<SimpleFileVO> simpleFileVOS = new ArrayList<>();
        if (null == path) {
            path = "";
        } else {
            path = path.substring(0, path.length() - 1);
        }
        List<Folder> folders = folderDao.getFoldersByPath(bucketId, path, keyword);
        simpleFolderVOS.addAll(folders.stream().map(a -> new SimpleFolderVO(a)).collect(Collectors.toList()));
        List<File> files = fileDao.getFilesByPath(bucketId, path, keyword);
        simpleFileVOS.addAll(files.stream().map(a -> new SimpleFileVO(a)).collect(Collectors.toList()));
        Map<String, List> res = new HashMap<>();
        res.put("folders", simpleFolderVOS);
        res.put("files", simpleFileVOS);
        return res;
    }

    @Override
    public void rmFiles(Long[] fileIds) {
        for (Long id : fileIds) {
            File file = fileDao.deleteFile(id);
            if (null != file) {
                safeDeleteFile(file.getNumber());
            }
        }
    }

    @Override
    public void rmFolders(Long[] folderIds) {
        // 直接使用like无需递归
        for (Long id : folderIds) {
            Folder folder = folderDao.findById(id);
            if (null != folder) {
                String path = folder.getPath().equals("") ? "" : folder.getPath() + "/";
                String pathPrefix = path + folder.getName();
                List<File> files = fileDao.getFilesInPath(folder.getBucketId(), pathPrefix);
                files.stream().forEach(file -> {
                    safeDeleteFile(file.getNumber());
                    file.delete();
                });
                List<Folder> folders = folderDao.getFoldersInPath(folder.getBucketId(), pathPrefix);
                folders.stream().forEach(f -> f.delete());
            }
            folder.delete();
        }
    }

    /**
     * delete file without throw IOException
     *
     * @param fileNumber
     */
    private void safeDeleteFile(String fileNumber) {
        try {
            fileTemplate.deleteFile(fileNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
