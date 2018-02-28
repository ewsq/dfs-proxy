package com.simu.dao;

import com.simu.model.Folder;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
public interface IFolderDao {
    Folder getFolderByPath(long bucketId, String path, String name);
    int deleteFoldersByBucketId(long bucketId);
    List<Folder> getFoldersByPath(long bucketId, String path, String keyword);
    Folder findById(long id);
    List<Folder> getFoldersInPath(long bucketId, String pathPrefix);
}
