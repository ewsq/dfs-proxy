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
@Table(name = "bucket")
public class Bucket extends ExecutableModel{
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int isPublic;
    private Timestamp createTime;
    private Timestamp modifyTime = TimeUtil.getCurrentSqlTime();

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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isPublic(){
        return this.isPublic == 1;
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
