package com.simu.service;


/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
public interface IBucketService {
    /**
     * hard remove bucket and related files and folders
     * @param id
     */
    void deleteBucket(long id);
}
