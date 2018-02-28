package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IBucketDao;
import com.simu.model.Bucket;
import com.simu.utils.TimeUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Bucket> getAllBuckets() {
        return DStatement.build(Bucket.class).selectList();
    }

    @Override
    public int update(long id, String name, int isPublic) {
        Map<String,Object> values = new HashMap<>();
        values.put("name", name);
        values.put("is_public",isPublic);
        values.put("modify_time", TimeUtil.getCurrentSqlTime());
        return DStatement.build(Bucket.class).id(id).set(values);
    }

    @Override
    public int delete(long id) {
        return DStatement.build(Bucket.class).id(id).remove();
    }

    @Override
    public Bucket findById(long id) {
        return DStatement.build(Bucket.class).id(id).selectOne();
    }
}
