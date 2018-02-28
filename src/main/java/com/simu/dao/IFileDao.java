package com.simu.dao;

import com.simu.model.File;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public interface IFileDao {
    /**
     *
     * @param bucketId
     * @param path
     * @param name
     * @return
     */
    File getFileByPath(long bucketId, String path, String name);

    /**
     *
     * @param bucketId
     * @return
     */
    List<File> getFilesByBucketId(long bucketId);

    /**
     * hard delete
     * @param id
     * @return
     */
    File deleteFile(long id);

    /**
     *
     * @param bucketId
     * @param path
     * @param keyword name前缀
     * @return
     */
    List<File> getFilesByPath(long bucketId, String path, String keyword);

    /**
     * 获取一个文件夹下的所有（包括子文件夹,子子文件夹...）文件
     * @param bucketId
     * @param pathPrefix
     * @return
     */
    List<File> getFilesInPath(long bucketId, String pathPrefix);
}
