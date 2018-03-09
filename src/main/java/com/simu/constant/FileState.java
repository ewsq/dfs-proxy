package com.simu.constant;

/**
 * @author DengrongGuan
 * @create 2018-03-07
 **/
public enum FileState {

    DEFAULT(0,"DEFAULT"),
    LARGE_FILE_UPLOADING(1, "LARGE_FILE_UPLOADING"),
    LARGE_FILE_UPLOADED(2, "LARGE_FILE_UPLOADED");

    private final int state;
    private final String name;
    private FileState(int state, String name){
        this.state = state;
        this.name = name;
    }

    public int getState(){
        return this.state;
    }
}
