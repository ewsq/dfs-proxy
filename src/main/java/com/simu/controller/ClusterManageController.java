package com.simu.controller;

import com.simu.dto.SimpleResponse;
import com.simu.seaweedfs.core.FileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DengrongGuan
 * @create 2018-03-15
 **/
@Controller
@RequestMapping(value = "/cluster-manage")
public class ClusterManageController {

    @Autowired
    FileSource fileSource;

    @RequestMapping(value = "/clusterState", method = RequestMethod.GET)
    @ResponseBody
    public SimpleResponse getClusterState() throws Exception{
        return SimpleResponse.ok(fileSource.getSystemClusterStatus());
    }
}
