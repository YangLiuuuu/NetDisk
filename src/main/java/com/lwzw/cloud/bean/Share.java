package com.lwzw.cloud.bean;

import java.util.Date;

public class Share {
    private Integer sid;

    private Integer fid;

    private Integer touid;

    private Integer fromuid;

    private Integer status;

    private String code;

    private Integer isread;

    private Date sharedate;

    private Integer likes;

    private Integer dislikes;

    private Integer ranks;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getTouid() {
        return touid;
    }

    public void setTouid(Integer touid) {
        this.touid = touid;
    }

    public Integer getFromuid() {
        return fromuid;
    }

    public void setFromuid(Integer fromuid) {
        this.fromuid = fromuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Date getSharedate() {
        return sharedate;
    }

    public void setSharedate(Date sharedate) {
        this.sharedate = sharedate;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Integer getRanks() {
        return ranks;
    }

    public void setRanks(Integer ranks) {
        this.ranks = ranks;
    }

    @Override
    public String toString() {
        return "Share{" +
                "sid=" + sid +
                ", fid=" + fid +
                ", touid=" + touid +
                ", fromuid=" + fromuid +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", isread=" + isread +
                ", sharedate=" + sharedate +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", ranks=" + ranks +
                '}';
    }
}