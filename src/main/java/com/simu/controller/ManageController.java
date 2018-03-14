package com.simu.controller;

import com.simu.dao.IBucketDao;
import com.simu.model.Bucket;
import com.simu.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
@Controller
public class ManageController {

    @Autowired
    IBucketDao bucketDao;

    @Autowired
    IFileService fileService;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/index")
    public String index(ModelMap map) {
        List<Bucket> buckets = bucketDao.getAllBuckets();
        map.addAttribute("buckets", buckets);
        return "index";
    }
    @RequestMapping("/master")
    public String master(){
        return "master";
    }
    @RequestMapping("/more")
    public String more(){
        return "more";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/bucket/{id}")
    public String bucket(@PathVariable("id")long id,
                         @RequestParam(value = "path", required = false)String path,
                         @RequestParam(value = "keyword", required = false)String keyword,
                         ModelMap map){
        Bucket bucket = bucketDao.findById(id);
        if (null == bucket){
            return "404";
        }
        List<String> paths = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        paths.add("根目录");
        String baseUrl = "/bucket/"+id+(keyword == null?"":"?keyword="+keyword);
        urls.add(baseUrl);
        if (null != path){
            String[] pathStrArray = path.split("/");
            baseUrl += keyword == null?"?path=":"&path=";
            for (String pathStr: pathStrArray){
                paths.add(pathStr);
                baseUrl += pathStr+"/";
                urls.add(baseUrl);
            }
        }
        urls.remove(urls.size() - 1);
        urls.add(null);
        Map<String,List> foldersAndFiles = fileService.getFileAndFoldersByPath(id, path, keyword);
        map.addAttribute("urls",urls);
        map.addAttribute("paths", paths);
        if (null == path){
            path = "";
        }
        if (null == keyword){
            keyword = "";
        }
        map.addAttribute("keyword",keyword);
        map.addAttribute("path",path);
        map.addAttribute("id",id);
        map.addAttribute("bucket",bucket);
        map.addAttribute("folders", foldersAndFiles.get("folders"));
        map.addAttribute("files", foldersAndFiles.get("files"));
        return "bucket";
    }
}
