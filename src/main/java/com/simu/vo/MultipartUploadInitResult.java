package com.simu.vo;

import com.simu.model.FileChunk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-03-08
 **/
public class MultipartUploadInitResult {
    private long fileId;
    private List<FileChunk> uploadedChunks = new ArrayList<>();

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public List<FileChunk> getUploadedChunks() {
        return uploadedChunks;
    }

    public void setUploadedChunks(List<FileChunk> uploadedChunks) {
        this.uploadedChunks = uploadedChunks;
    }
}
