package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IFolderDao;
import com.simu.model.Folder;
import org.springframework.stereotype.Repository;

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
}
