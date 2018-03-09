package com.simu.vo;

import com.simu.model.File;

import java.sql.Timestamp;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
public class SimpleFileVO{
    private long id;
    private String name;
    private Timestamp modifyTime;
    private long size;
    private String number;
    private String downloadPath;

    public SimpleFileVO(){}

    public SimpleFileVO(File file){
        this.id = file.getId();
        this.name = file.getName();
        this.size = file.getSize();
        this.number = file.getNumber();
        this.modifyTime = file.getModifyTime();
        this.downloadPath = file.getPath().equals("")?this.name:file.getPath()+"/"+this.name;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
