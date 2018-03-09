package com.simu.controller;

import com.simu.dto.SimpleResponse;
import com.simu.seaweedfs.core.FileSource;
import com.simu.seaweedfs.core.FileTemplate;
import com.simu.service.IFileService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author DengrongGuan
 * @create 2018-02-06 上午10:10
 **/
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    IFileService fileService;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> download(@RequestParam(value = "Path") String path,
                                           @RequestParam(value = "Bucket") String bucket,
                                           @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                                           @RequestParam(value = "Expires", required = false) Long expires,
                                           @RequestParam(value = "Signature", required = false) String signature) throws Exception{
        //下载文件
//        return path + "?"+accessKeyId + "&" + expires + "&"+signature;
//        String pattern = (String)
//                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
//
//        String searchTerm = new AntPathMatcher().extractPathWithinPattern(pattern,
//                request.getServletPath());
//        throw new NullPointerException();
//        try {
//            return URLDecoder.decode(request.getServletPath(),"utf-8") + "?"+accessKeyId + "&" + expires + "&"+signature;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        //6,0105d9b2e7
        //2,02e1b65433

        return fileService.getFile(path, bucket, accessKeyId, expires, signature);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse uploadFile(@RequestParam(value = "Path") String path,
                                     @RequestParam(value = "Bucket") String bucket,
                                     @RequestParam(value = "File")MultipartFile file,
                                     @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                                     @RequestParam(value = "Expires", required = false) Long expires,
                                     @RequestParam(value = "Signature", required = false) String signature,
                                     @RequestParam(value = "Callback", required = false)String callbackUrl) throws Exception{
        if (expires == null){
            expires = 0L;
        }
        fileService.putFile(file,path,bucket,accessKeyId,expires,signature);
        return SimpleResponse.ok(null);
    }

    @RequestMapping(value = "/initMultipartUpload", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse initMultipartUpload(@RequestParam(value = "Path") String path,
                                              @RequestParam(value = "Bucket") String bucket,
                                              @RequestParam(value = "Size") long size,
                                              @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                                              @RequestParam(value = "Expires", required = false) Long expires,
                                              @RequestParam(value = "Signature", required = false) String signature,
                                              @RequestParam(value = "Callback", required = false)String callbackUrl) throws Exception{
        if (expires == null){
            expires = 0L;
        }
        return SimpleResponse.ok(fileService.initMultipartUpload(size, path, bucket, accessKeyId,expires, signature));
    }

    @RequestMapping(value = "/uploadPart", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse uploadPart(@RequestParam(value = "FileId") long fileId,
                                     @RequestParam(value = "Offset") long offset,
                                     @RequestParam(value = "Size") long size,
                                     @RequestParam(value = "Bucket") String bucket,
                                     @RequestParam(value = "Path") String path,
                                     @RequestParam(value = "File")MultipartFile file,
                                     @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                                     @RequestParam(value = "Expires", required = false) Long expires,
                                     @RequestParam(value = "Signature", required = false) String signature,
                                     @RequestParam(value = "Callback", required = false)String callbackUrl)throws Exception{
        if (expires == null){
            expires = 0L;
        }
        fileService.putFileChunk(file, path, bucket, fileId, offset, size, accessKeyId, expires, signature);
        return SimpleResponse.ok(null);
    }

    @RequestMapping(value = "/completeMultipartUpload", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse completeMultipartUpload(@RequestParam(value = "FileId") long fileId,
                                                  @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                                                  @RequestParam(value = "Expires", required = false) Long expires,
                                                  @RequestParam(value = "Signature", required = false) String signature,
                                                  @RequestParam(value = "Callback", required = false)String callbackUrl){
        if (expires == null){
            expires = 0L;
        }
        return SimpleResponse.ok(null);
    }



}
