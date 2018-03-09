package com.simu.dao.impl;

import com.simu.dao.DStatement;
import com.simu.dao.IFileChunkDao;
import com.simu.model.FileChunk;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-03-08
 **/
@Repository
public class FileChunkDao implements IFileChunkDao{

    @Override
    public List<FileChunk> getFileChunksByFileId(long fileId) {
        return DStatement.build(FileChunk.class).fileId(fileId).selectList();
    }

    @Override
    public void deleteChunksByFileId(long fileId) {
        DStatement.build(FileChunk.class).fileId(fileId).remove();
    }
}
