package com.simu.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {
    @Test
    public void getSimpleFileName() throws Exception {
    }

    @Test
    public void getPurePath() throws Exception {
        Assert.assertEquals("a/b/c",FileUtil.getPurePath("a/b/c/d.txt"));
    }

}