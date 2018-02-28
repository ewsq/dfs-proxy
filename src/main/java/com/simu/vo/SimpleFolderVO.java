package com.simu.vo;

import com.simu.model.Folder;

/**
 * @author DengrongGuan
 * @create 2018-02-27
 **/
public class SimpleFolderVO {
    private long id;
    private String name;

    public SimpleFolderVO(){}

    public SimpleFolderVO(long id, String name){
        this.id = id;
        this.name = name;
    }

    public SimpleFolderVO(Folder folder){
        this.id = folder.getId();
        this.name = folder.getName();
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
}
