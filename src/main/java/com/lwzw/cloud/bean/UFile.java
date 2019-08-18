package com.lwzw.cloud.bean;

import java.util.Date;

public class UFile {
    private Integer ufid;

    private Integer uid;

    private Integer fid;

    private String name;

    private Date savedate;

    public UFile() {
    }

    public UFile(Integer uid, Integer fid, String name,Date saveDate) {
        this.uid = uid;
        this.fid = fid;
        this.name = name;
        this.savedate = saveDate;
    }

    public Integer getUfid() {
        return ufid;
    }

    public void setUfid(Integer ufid) {
        this.ufid = ufid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getSavedate() {
        return savedate;
    }

    public void setSavedate(Date savedate) {
        this.savedate = savedate;
    }

    @Override
    public String toString() {
        return "UFile{" +
                "ufid=" + ufid +
                ", uid=" + uid +
                ", fid=" + fid +
                ", name='" + name + '\'' +
                ", savedate=" + savedate +
                '}';
    }
}