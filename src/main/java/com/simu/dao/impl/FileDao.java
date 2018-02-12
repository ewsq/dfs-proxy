package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IFileDao;
import com.simu.model.File;
import org.springframework.stereotype.Repository;

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
}
