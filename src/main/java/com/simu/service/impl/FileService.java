package com.simu.service.impl;

import com.alibaba.fastjson.JSON;
import com.simu.constant.FileState;
import com.simu.constant.ResponseCodeEnum;
import com.simu.dao.*;
import com.simu.exception.ErrorCodeException;
import com.simu.model.Bucket;
import com.simu.model.File;
import com.simu.model.FileChunk;
import com.simu.model.Folder;
import com.simu.seaweedfs.core.FileSource;
import com.simu.seaweedfs.core.FileTemplate;
import com.simu.seaweedfs.core.contect.AssignFileKeyResult;
import com.simu.seaweedfs.core.file.ChunkInfo;
import com.simu.seaweedfs.core.file.ChunkManifest;
import com.simu.seaweedfs.core.file.FileHandleStatus;
import com.simu.service.IFileService;
import com.simu.utils.FileUtil;
import com.simu.utils.TimeUtil;
import com.simu.vo.MultipartUploadInitResult;
import com.simu.vo.SimpleFileVO;
import com.simu.vo.SimpleFolderVO;
import org.apache.http.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    IFileChunkDao fileChunkDao;

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
    public ResponseEntity getFilesZip(List<String> paths,String bucket, String zipName, String accessId, Long expires, String signature) throws Exception {
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (buck.getIsPublic() == 0) {
            // 私密
            List<String> resources = paths.stream().map(p -> "/" + bucket + "/" + p).collect(Collectors.toList());
            authDao.validSignature(resources, accessId, expires, signature, RequestMethod.GET);
        }
        zipName += ".zip";
        byte[] buffer = new byte[1024];
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);
            for (String path: paths){
                String fileName = FileUtil.getSimpleFileName(path);
                String purePath = FileUtil.getPurePath(path);
                File fileEntity = fileDao.getFileByPath(buck.getId(), purePath, fileName);
                if (null == fileEntity) {
                    throw new ErrorCodeException(ResponseCodeEnum.FILE_NOT_EXIST);
                }
                InputStream inputStream = fileTemplate.getFileStream(fileEntity.getNumber()).getInputStream();
                ZipEntry ze= new ZipEntry(fileName);
                zos.putNextEntry(ze);
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                inputStream.close();
                zos.closeEntry();
            }
            byteArrayOutputStream.flush();
            zos.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String downloadFileName = new String(zipName.getBytes("UTF-8"),"iso-8859-1");
            headers.setContentDispositionFormData("attachment", downloadFileName);
            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
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

    @Override
    public void putFileChunk(MultipartFile file, String path, String bucket, long fileId, long offset, long size, String accessId, Long expires, String signature) throws Exception{
        String resource = "/" + bucket + "/" + path;
        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.POST);
        putFileChunk(file, path, bucket, fileId, offset, size);
    }

    @Override
    public void putFileChunk(MultipartFile file, String path, String bucket, long fileId, long offset, long size) throws Exception {
        FileHandleStatus fileHandleStatus = fileTemplate.saveFileByStream(fileId+"-offset-"+offset, file.getInputStream(), bucket);
        FileChunk fileChunk = new FileChunk(fileHandleStatus.getFileId(), fileId, offset, size);
        fileChunk.save();
    }

    @Override
    public MultipartUploadInitResult initMultipartUpload(long size, String path, String bucket, String accessId, Long expires, String signature) throws Exception {
        String resource = "/" + bucket + "/" + path;
        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.POST);
        return initMultipartUpload(size, path,bucket);
    }

    @Override
    public MultipartUploadInitResult initMultipartUpload(long size, String path, String bucket) throws Exception {
        Bucket buck = bucketDao.getBucketByName(bucket);
        if (null == buck){
            throw new ErrorCodeException(ResponseCodeEnum.BUCKET_NOT_EXIST);
        }
        String fileName = FileUtil.getSimpleFileName(path);
        String purePath = FileUtil.getPurePath(path);
        File fileEntity = fileDao.getFileByPath(buck.getId(), purePath, fileName);
        MultipartUploadInitResult multipartUploadInitResult = new MultipartUploadInitResult();
        if (null != fileEntity){
            //1. 原文件上传成功-删除原文件返回文件id 2.原文件正在上传，返回已上传的分片列表
            if (fileEntity.getState() == FileState.LARGE_FILE_UPLOADED.getState() || fileEntity.getState() == FileState.DEFAULT.getState()){
                fileTemplate.deleteFile(fileEntity.getNumber());// 会自动删除分片
                fileChunkDao.deleteChunksByFileId(fileEntity.getId());
                AssignFileKeyResult assignFileKeyResult = fileTemplate.assignFileKeyResult();
                fileEntity.setState(FileState.LARGE_FILE_UPLOADING.getState());
                fileEntity.setNumber(assignFileKeyResult.getFid());
                fileEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
                fileEntity.setSize(size);
                fileEntity.update();
            }else{
                List<FileChunk> fileChunks = fileChunkDao.getFileChunksByFileId(fileEntity.getId());
                multipartUploadInitResult.setUploadedChunks(fileChunks);
            }
        }else{
//            AssignFileKeyResult assignFileKeyResult = fileTemplate.assignFileKeyResult();
            fileEntity = new File(fileName, "", purePath, size, buck.getId());
            fileEntity.setFolderId(createFolders(purePath, buck.getId()));
            fileEntity.setState(FileState.LARGE_FILE_UPLOADING.getState());
            fileEntity.save();
        }
        multipartUploadInitResult.setFileId(fileEntity.getId());
        return multipartUploadInitResult;
    }

    @Override
    public void completeMultipartUpload(long fileId, String path, String bucket, String accessId, Long expires, String signature) throws Exception{
        String resource = "/" + bucket + "/" + path;
        authDao.validSignature(resource, accessId, expires, signature, RequestMethod.POST);
        completeMultipartUpload(fileId, path, bucket);
    }

    @Override
    public void completeMultipartUpload(long fileId, String path, String bucket) throws Exception {
        File file = fileDao.findById(fileId);
        if (null == file){
            throw new ErrorCodeException(ResponseCodeEnum.FILE_NOT_EXIST);
        }
        List<FileChunk> fileChunks = fileChunkDao.getFileChunksByFileId(fileId);
        String mime = new MimetypesFileTypeMap().getContentType(file.getName());
        ChunkManifest chunkManifest = new ChunkManifest();
        chunkManifest.setName(file.getName());
        chunkManifest.setSize(file.getSize());
        chunkManifest.setMime(mime);
        List<ChunkInfo> chunkInfos = fileChunks.stream().map(fileChunk -> {
            ChunkInfo chunkInfo = new ChunkInfo();
            chunkInfo.setFid(fileChunk.getNumber());
            chunkInfo.setOffset(fileChunk.getOffset());
            chunkInfo.setSize(fileChunk.getSize());
            return chunkInfo;
        }).collect(Collectors.toList());
        chunkManifest.setChunks(chunkInfos);
        String chunkManifestStr = JSON.toJSONString(chunkManifest);
        FileHandleStatus fileHandleStatus = fileTemplate.saveFileByString(file.getName(), chunkManifestStr);
        file.setModifyTime(TimeUtil.getCurrentSqlTime());
        file.setNumber(fileHandleStatus.getFileId());
        file.setState(FileState.LARGE_FILE_UPLOADED.getState());
        file.update();
    }

    public long createFolders(String purePath, long bucketId) {
        purePath = purePath.replace(" ", "");
        if ("".equals(purePath)) {
            //根目录不用创建
            return 0;
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
                if (file.getState() == FileState.LARGE_FILE_UPLOADING.getState()){
                    List<FileChunk> fileChunks = fileChunkDao.getFileChunksByFileId(id);
                    fileChunks.forEach(fileChunk -> safeDeleteFile(fileChunk.getNumber()));
                    //先将实际的碎片清理再删库
                    fileChunkDao.deleteChunksByFileId(id);
                }else{
                    fileChunkDao.deleteChunksByFileId(id);
                    safeDeleteFile(file.getNumber());
                }
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
