package com.simu.dao;

import org.exemodel.orm.Statement;

/**
 * Created by DengrongGuan
 */
public class DStatement extends Statement<DStatement> {
    public static DStatement build(Class modelClass){
        return new DStatement(modelClass);
    }

    public DStatement(Class<?> clazz) {
        super(clazz);
    }

    public DStatement partitionId(Object value){
        return eq(getModelMeta().getPartitionColumn().columnName,value);
    }

    public DStatement pathPrefix(String path) {
        return like("path", path + "%");
    }

    public DStatement parentId(long id) {
        return eq("parentId", id);
    }

    public DStatement folderId(long id) {
        return eq("folderId", id);
    }

    public DStatement name(String name) {
        return eq("name", name);
    }

    public DStatement path(String path){
        return eq("path", path);
    }

    public DStatement bucketId(long bucketId){
        return eq("bucket_id", bucketId);
    }

}
