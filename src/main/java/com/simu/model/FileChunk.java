package com.simu.model;

import org.exemodel.orm.ExecutableModel;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author DengrongGuan
 * @create 2018-03-07
 **/
@Table(name = "file_chunk")
public class FileChunk extends ExecutableModel {
    @Id
    @GeneratedValue
    private long id;
    private String number;
    private long fileId;
    private long offset;
    private long size;

    public FileChunk(){}

    public FileChunk(String number, long fileId, long offset, long size) {
        this.number = number;
        this.fileId = fileId;
        this.offset = offset;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
