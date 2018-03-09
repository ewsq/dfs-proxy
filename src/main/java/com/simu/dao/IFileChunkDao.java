package com.simu.dao;

import com.simu.model.FileChunk;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-03-08
 **/
public interface IFileChunkDao {
    List<FileChunk> getFileChunksByFileId(long fileId);
    void deleteChunksByFileId(long fileId);
}
