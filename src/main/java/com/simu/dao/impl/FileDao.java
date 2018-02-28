package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IFileDao;
import com.simu.model.File;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
@Repository
public class FileDao implements IFileDao {

    @Override
    public File getFileByPath(long bucketId, String path, String name) {
        return DStatement.build(File.class).bucketId(bucketId).path(path).name(name).selectOne();
    }

    @Override
    public List<File> getFilesByBucketId(long bucketId) {
        return DStatement.build(File.class).bucketId(bucketId).selectList();
    }

    @Override
    public File deleteFile(long id) {
        File file = DStatement.build(File.class).id(id).selectOne();
        file.delete();
        return file;
    }

    @Override
    public List<File> getFilesByPath(long bucketId, String path, String keyword) {
        DStatement dStatement = DStatement.build(File.class).bucketId(bucketId).path(path);
        if (null != keyword){
            dStatement = dStatement.like("name",keyword+"%");
        }
        return dStatement.selectList();
    }

    @Override
    public List<File> getFilesInPath(long bucketId, String pathPrefix) {
        return DStatement.build(File.class).bucketId(bucketId).pathPrefix(pathPrefix).selectList();
    }


}
