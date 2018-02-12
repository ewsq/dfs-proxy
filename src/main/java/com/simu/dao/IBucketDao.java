package com.simu.dao;

import com.simu.model.Bucket;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
public interface IBucketDao {
    Bucket getBucketByName(String name);
}
