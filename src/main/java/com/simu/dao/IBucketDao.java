package com.simu.dao;

import com.simu.model.Bucket;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
public interface IBucketDao {
    Bucket getBucketByName(String name);
    List<Bucket> getAllBuckets();
    int update(long id, String name, int isPublic);
    int delete(long id);
    Bucket findById(long id);
}
