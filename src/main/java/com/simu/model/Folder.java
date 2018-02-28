package com.simu.model;

import com.simu.utils.TimeUtil;
import org.exemodel.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Table(name = "folder")
public class Folder extends ExecutableModel {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String path;// 路径：路径：null(根路径)  或者 a/b/c
    private long parentId;
    private long bucketId;
    private Timestamp createTime;
    private Timestamp modifyTime = TimeUtil.getCurrentSqlTime();

    public Folder(){}

    public Folder(String name, String path, long parentId, long bucketId) {
        this.name = name;
        this.path = path;
        this.parentId = parentId;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
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
