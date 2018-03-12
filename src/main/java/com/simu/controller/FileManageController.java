package com.simu.controller;

import com.simu.dto.SimpleResponse;
import com.simu.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
@Controller
@RequestMapping("/file-manage")
public class FileManageController {

    @Autowired
    IFileService fileService;

    /**
     * 获取一个文件夹下的文件和文件夹(使用path的目的是减少一次获取目录导航的请求且更加直观)
     * @param bucketId
     * @param path null表示根目录；a/b/c/
     * @param keyword 前缀匹配
     * @return
     */
    @RequestMapping(value = "/children", method = RequestMethod.GET)
    @ResponseBody
    public SimpleResponse getChildren(@RequestParam("bucketId")long bucketId,
                                      @RequestParam(value = "path", required = false)String path,
                                      @RequestParam(value = "keyword", required = false)String keyword){
        return SimpleResponse.ok(fileService.getFileAndFoldersByPath(bucketId, path, keyword));
    }


    /**
     * 新建目录(类似 mkdir -p)
     * @param bucketId
     * @param path a/b/c
     * @return c 的 id
     */
    @RequestMapping(value = "/createFolders", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse createFolders(@RequestParam("bucketId")long bucketId,
                                       @RequestParam("path")String path){
        return SimpleResponse.ok(fileService.createFolders(path, bucketId));
    }

    /**
     * 批量删除文件
     * @param fileIds form-data ids:1,2,3,4,5
     * @return
     */
    @RequestMapping(value = "/removeFiles", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse rmFiles(@RequestParam(value = "ids")Long[] fileIds){
        fileService.rmFiles(fileIds);
        return SimpleResponse.ok(1);
    }

    /**
     * 批量删除文件夹(会删除文件夹下的所有文件夹和文件,类似 rm -rf)
     * @param folderIds
     * @return
     */
    @RequestMapping(value = "/removeFolders", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse rmFolders(@RequestParam(value = "ids")Long[] folderIds){
        fileService.rmFolders(folderIds);
        return SimpleResponse.ok(1);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam(value = "Path") String path,
                                           @RequestParam(value = "Bucket") String bucket) throws Exception{
        return fileService.getFile(path, bucket);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse uploadFile(@RequestParam(value = "Path") String path,
                                     @RequestParam(value = "Bucket") String bucket,
                                     @RequestParam(value = "File")MultipartFile file) throws Exception{
        fileService.putFile(file,path,bucket);
        return SimpleResponse.ok(null);
    }

    @RequestMapping(value = "/initMultipartUpload")
    @ResponseBody
    public SimpleResponse initMultipartUpload(@RequestParam(value = "Path") String path,
                                              @RequestParam(value = "Bucket") String bucket,
                                              @RequestParam(value = "Size") long size) throws Exception{
        return SimpleResponse.ok(fileService.initMultipartUpload(size, path, bucket));
    }

    @RequestMapping(value = "/uploadPart", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse uploadPart(@RequestParam(value = "FileId") long fileId,
                                     @RequestParam(value = "Offset") long offset,
                                     @RequestParam(value = "Size") long size,
                                     @RequestParam(value = "Bucket") String bucket,
                                     @RequestParam(value = "Path") String path,
                                     @RequestParam(value = "File")MultipartFile file) throws Exception{
        fileService.putFileChunk(file, path, bucket, fileId, offset, size);
        return SimpleResponse.ok(null);
    }

    @RequestMapping(value = "/completeMultipartUpload", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse completeMultipartUpload(@RequestParam(value = "FileId") long fileId,
                                                  @RequestParam(value = "Bucket")String bucket,
                                                  @RequestParam(value = "Path")String path)throws Exception{
        fileService.completeMultipartUpload(fileId, path, bucket);
        return SimpleResponse.ok(null);
    }


}
