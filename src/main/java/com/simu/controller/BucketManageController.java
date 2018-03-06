package com.simu.controller;

import com.simu.dao.IBucketDao;
import com.simu.dto.SimpleResponse;
import com.simu.model.Bucket;
import com.simu.service.IBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
@Controller
@RequestMapping("/bucket-manage")
public class BucketManageController {

    @Autowired
    IBucketDao bucketDao;

    @Autowired
    IBucketService bucketService;

    @RequestMapping(value = "/buckets", method = RequestMethod.GET)
    @ResponseBody
    public SimpleResponse getBuckets(){
        return SimpleResponse.ok(bucketDao.getAllBuckets());
    }

    @RequestMapping(value = "/updateBucket", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse updateBucket(@RequestBody Bucket bucket){
        return SimpleResponse.checkBoolean(bucketDao.update(bucket.getId(), bucket.getName(), bucket.getIsPublic()) > 0);
    }

    /**
     * hard delete(remove all related files and folders)
     * @param id
     * @return
     */
    @RequestMapping(value = "/bucket", method = RequestMethod.DELETE)
    @ResponseBody
    public SimpleResponse deleteBucket(@RequestParam("id")long id){
        bucketService.deleteBucket(id);
        return SimpleResponse.ok(1);
    }

    @RequestMapping(value = "/addBucket", method = RequestMethod.POST)
    @ResponseBody
    public SimpleResponse addBucket(@RequestBody Bucket bucket){
        bucket.checkParam();
        bucket.save();
        return SimpleResponse.ok(1);
    }


}
