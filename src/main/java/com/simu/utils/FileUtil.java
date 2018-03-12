package com.simu.utils;

import com.alibaba.druid.util.StringUtils;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
public class FileUtil {
    /**
     *
     * @param path a/b/c/d.txt
     * @return d.txt
     */
    public static String getSimpleFileName(String path){
        if (StringUtils.isEmpty(path)){
            return "";
        }
        int index = path.lastIndexOf('/');
        if (index == -1){
            return path;
        }
        String fileName = "";
        if (index > 0){
            fileName = path.substring(index+1);
        }
        return fileName;
    }


    /**
     *
     * @param path a/b/c/d.txt
     * @return a/b/c
     */
    public static String getPurePath(String path){
        if (StringUtils.isEmpty(path)){
            return "";
        }
        int index = path.lastIndexOf('/');
        String purePath = "";
        if (index > 0){
            purePath = path.substring(0,index);
        }
        return purePath;
    }
}
