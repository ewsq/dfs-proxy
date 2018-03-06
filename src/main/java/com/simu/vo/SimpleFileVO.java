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
    private double size;
    private String number;

    public SimpleFileVO(){}

    public SimpleFileVO(File file){
        this.id = file.getId();
        this.name = file.getName();
        this.size = file.getSize();
        this.number = file.getNumber();
        this.modifyTime = file.getModifyTime();
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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}