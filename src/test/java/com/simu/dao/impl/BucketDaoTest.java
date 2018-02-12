package com.simu.dao.impl;

import com.simu.BaseTest;
import com.simu.dao.IBucketDao;
import com.simu.model.Bucket;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BucketDaoTest extends BaseTest {
    @Autowired
    IBucketDao bucketDao;
    @Test
    public void getBucketByName() throws Exception {
        Bucket bucket = bucketDao.getBucketByName("mkpri");
        Assert.assertEquals(bucket.getName(), "mkpri");
    }

    @Test
    public void addBucket(){
        Bucket bucket = new Bucket();
        bucket.setIsPublic(1);
        bucket.setName("abc");
        bucket.save();
        System.out.println(bucket.getId());
    }

}