package com.simu.service.impl;

import com.simu.dao.IBucketDao;
import com.simu.dao.IFolderDao;
import com.simu.service.IBucketService;
import com.simu.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
@Service
public class BucketService implements IBucketService{

    @Autowired
    IBucketDao bucketDao;

    @Autowired
    IFileService fileService;

    @Autowired
    IFolderDao folderDao;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void deleteBucket(long id) {
        bucketDao.delete(id);
        folderDao.deleteFoldersByBucketId(id);
        fileService.deleteFilesByBucketId(id);
    }
}
