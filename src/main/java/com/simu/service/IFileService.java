package com.simu.service;

import com.simu.model.FileChunk;
import com.simu.vo.MultipartUploadInitResult;
import com.simu.vo.SimpleFileVO;
import com.simu.vo.SimpleFolderVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
public interface IFileService {
    /**
     * 获取文件流(用户版需认证)
     * @param path
     * @param bucket
     * @param accessId
     * @param expires
     * @param signature
     * @return
     */
    ResponseEntity getFile(String path, String bucket, String accessId, Long expires, String signature) throws Exception;

    /**
     *
     * @param path
     * @param bucket
     * @return
     * @throws Exception
     */
    ResponseEntity getFile(String path,String bucket) throws Exception;

    /**
     * 上传文件流(用户版需认证)
     * @param file
     * @param path affair/112/sjdhJsdf/hello.txt
     * @param bucket
     * @param accessId
     * @param expires long seconds
     * @param signature
     * @throws Exception
     */
    void putFile(MultipartFile file, String path, String bucket, String accessId, Long expires, String signature) throws Exception;

    /**
     *
     * @param file
     * @param path
     * @param bucket
     * @return
     */
    void putFile(MultipartFile file,String path,String bucket) throws Exception;

    /**
     * 上传分片小文件(每个分片上传都需要验证)
     * 碎片清理：1. 主动 2. 定时清理
     * @param bucket
     * @param file
     * @param fileId
     * @param offset
     * @param size
     */
    void putFileChunk(MultipartFile file, String path, String bucket, long fileId, long offset, long size, String accessKeyId, Long expires, String signature) throws Exception;

    /**
     * 初始化大文件上传(用户版需认证)
     * 1. 正在上传的同名文件，直接返回已上传成功的分片列表
     * 2. 上传完成的同名文件直接删除文件和分片（测试是否能自动删除分片）
     * @param size
     * @param path
     * @param bucket
     * @param accessId
     * @param expires
     * @param signature
     * @return
     */
    MultipartUploadInitResult initMultipartUpload(long size, String path, String bucket, String accessId, Long expires, String signature) throws Exception;

    /**
     * 完成大文件上传(用户版需认证)
     * @param fileId
     * @param accessId
     * @param expires
     * @param signature
     */
    void completeMultipartUpload(long fileId, String accessId, Long expires, String signature) throws Exception;

    /**
     * 返回最下层的folderId
     * @param purePath a/b/c
     * @param bucketId 2
     * @return c 的id
     */
    long createFolders(String purePath, long bucketId);

    /**
     *
     * @param bucketId
     * @throws Exception from fileTemplate.deleteFile
     */
    void deleteFilesByBucketId(long bucketId);

    /**
     * 获取文件夹下的文件和文件夹
     * @param bucketId
     * @param path "a/b/c/" null
     * @param keyword 可能为null
     * @return
     */
    Map<String,List> getFileAndFoldersByPath(long bucketId, String path, String keyword);

    /**
     * 批量删除文件,有出错的直接抛出异常
     * @param fileIds
     */
    void rmFiles(Long[] fileIds);

    /**
     * 批量删除文件夹,有出错的直接抛异常
     * @param folderIds
     */
    void rmFolders(Long[] folderIds);
}
