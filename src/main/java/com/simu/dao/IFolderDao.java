package com.simu.dao;

import com.simu.model.Folder;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
public interface IFolderDao {
    Folder getFolderByPath(long bucketId, String path, String name);
}
