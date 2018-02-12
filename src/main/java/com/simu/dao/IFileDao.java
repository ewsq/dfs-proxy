package com.simu.dao;

import com.simu.model.File;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public interface IFileDao {
    File getFileByPath(long bucketId, String path, String name);
}
