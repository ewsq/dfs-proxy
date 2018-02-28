package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IFolderDao;
import com.simu.model.Folder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
@Repository
public class FolderDao implements IFolderDao {
    @Override
    public Folder getFolderByPath(long bucketId, String path, String name) {
        return DStatement.build(Folder.class).bucketId(bucketId).path(path).name(name).selectOne();
    }

    @Override
    public int deleteFoldersByBucketId(long bucketId) {
        return DStatement.build(Folder.class).bucketId(bucketId).remove();
    }

    @Override
    public List<Folder> getFoldersByPath(long bucketId, String path, String keyword) {
        DStatement dStatement = DStatement.build(Folder.class).bucketId(bucketId).path(path);
        if (null != keyword){
            dStatement.like("name", keyword+"%");
        }
        return dStatement.selectList();
    }

    @Override
    public Folder findById(long id) {
        return DStatement.build(Folder.class).findById(id);
    }

    @Override
    public List<Folder> getFoldersInPath(long bucketId, String pathPrefix) {
        return DStatement.build(Folder.class).bucketId(bucketId).pathPrefix(pathPrefix).selectList();
    }
}
