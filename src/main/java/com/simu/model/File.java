package com.simu.model;

import com.simu.utils.TimeUtil;
import org.exemodel.orm.ExecutableModel;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Table(name = "file")
public class File extends ExecutableModel {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String number;//weed中的文件id
    private String path;// 路径：null(根路径)  或者 a/b/c
    private long size;// Byte, 存储在 weed 中的大小 可能会被压缩
    private long folderId;
    private long bucketId;
    private int state;// 状态, 0:默认, 1:大文件正在上传，2：大文件上传成功
    private Timestamp createTime;
    private Timestamp modifyTime = TimeUtil.getCurrentSqlTime();

    public File(){}

    /**
     *
     * @param name
     * @param number
     * @param path
     * @param size
     * @param bucketId
     */
    public File(String name, String number, String path, long size, long bucketId) {
        this.name = name;
        this.number = number;
        this.path = path;
        this.size = size;
        this.bucketId = bucketId;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getFolderId() {
        return folderId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }
}
