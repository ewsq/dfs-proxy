package com.simu.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
public interface IFileService {
    /**
     * 获取文件流
     * @param path
     * @param bucket
     * @param accessId
     * @param expires
     * @param signature
     * @return
     */
    ResponseEntity getFile(String path, String bucket, String accessId, long expires, String signature) throws Exception;

    /**
     * 上传文件流
     * @param file
     * @param path affair/112/sjdhJsdf/hello.txt
     * @param bucket
     * @param accessId
     * @param expires long seconds
     * @param signature
     * @throws Exception
     */
    void putFile(CommonsMultipartFile file, String path, String bucket, String accessId, long expires, String signature) throws Exception;

    /**
     * 返回最下层的folderId
     * @param purePath a/b/c
     * @param bucketId 2
     * @return c 的id
     */
    long createFolders(String purePath, long bucketId);
}
