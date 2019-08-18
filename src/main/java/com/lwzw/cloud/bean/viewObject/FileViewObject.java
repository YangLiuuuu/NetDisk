package com.lwzw.cloud.bean.viewObject;

import java.util.Date;

public class FileViewObject {
    private Integer ufid;
    private Integer uid;
    private Integer fid;
    private String fileName;
    private Date saveDate;
    private Double size;
    private Integer levels;

    public FileViewObject() {
    }

    public FileViewObject(Integer ufid, Integer uid, Integer fid, String fileName, Date saveDate, Double size, Integer levels) {
        this.ufid = ufid;
        this.uid = uid;
        this.fid = fid;
        this.fileName = fileName;
        this.saveDate = saveDate;
        this.size = size;
        this.levels = levels;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "FileViewObject{" +
                "ufid=" + ufid +
                ", uid=" + uid +
                ", fid=" + fid +
                ", fileName='" + fileName + '\'' +
                ", saveDate=" + saveDate +
                ", size=" + size +
                ", levels=" + levels +
                '}';
    }
}
