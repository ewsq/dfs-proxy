package com.simu.service.impl;

import com.simu.BaseTest;
import com.simu.dao.IBucketDao;
import com.simu.service.IFileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.MimetypesFileTypeMap;

import static org.junit.Assert.*;

public class FileServiceTest extends BaseTest{
    @Autowired
    IFileService fileService;
    @Test
    public void createFolders() throws Exception {
        fileService.createFolders("a/ /c",1);
    }
    @Test
    public void getContentType(){
        System.out.println(new MimetypesFileTypeMap().getContentType("abc.txt"));
    }

}