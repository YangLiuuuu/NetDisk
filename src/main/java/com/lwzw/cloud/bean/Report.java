package com.lwzw.cloud.bean;

public class Report {
    private Integer rid;

    private Integer fid;

    private Integer uid;

    private String desc;

    private Boolean isread;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public Boolean getIsread() {
        return isread;
    }

    public void setIsread(Boolean isread) {
        this.isread = isread;
    }

    @Override
    public String toString() {
        return "Report{" +
                "rid=" + rid +
                ", fid=" + fid +
                ", uid=" + uid +
                ", desc='" + desc + '\'' +
                ", isread=" + isread +
                '}';
    }
}