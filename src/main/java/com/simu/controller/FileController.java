package com.simu.controller;

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
                                           @RequestParam(value = "Expires", required = false) long expires,
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

    @RequestMapping(value = "/upload", method = RequestMethod.PUT)
    @ResponseBody
    public String uploadFile(@RequestParam(value = "Path") String path,
                             @RequestParam(value = "Bucket") String bucket,
                             @RequestParam(value = "File")CommonsMultipartFile file,
                             @RequestParam(value = "AccessKeyId", required = false) String accessKeyId,
                             @RequestParam(value = "Expires", required = false) long expires,
                             @RequestParam(value = "Signature", required = false) String signature,
                             @RequestParam(value = "Callback", required = false)String callbackUrl){

        return "";
    }
}
