package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IBucketDao;
import com.simu.model.Bucket;
import org.springframework.stereotype.Repository;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Repository
public class BucketDao implements IBucketDao{
    @Override
    public Bucket getBucketByName(String name) {
        return DStatement.build(Bucket.class).name(name).selectOne();
    }
}
