package com.lwzw.cloud.bean.viewObject;

import java.util.Date;

public class ShareDetailViewObject {
    private Integer sid;//分享条目id
    private Integer upid;//分享人id
    private String shareUser;//分享者用户名
    private String fileName;//文件名
    private Date shareDate;//分享日期
    private Integer likes;//好评数
    private Integer dislikes;//差评数
    private Integer ranks;//保存等级
    private Integer status;//评价状态，有差评，好评，取消评价三种，默认为取消/为评价

    public ShareDetailViewObject(Integer sid, Integer upid, String shareUser, String fileName, Date shareDate, Integer likes, Integer dislike, Integer ranks, Integer status) {
        this.sid = sid;
        this.upid = upid;
        this.shareUser = shareUser;
        this.fileName = fileName;
        this.shareDate = shareDate;
        this.likes = likes;
        this.dislikes = dislike;
        this.ranks = ranks;
        this.status = status;
    }

    public ShareDetailViewObject() {
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUpid() {
        return upid;
    }

    public void setUpid(Integer upid) {
        this.upid = upid;
    }

    public String getShareUser() {
        return shareUser;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ShareDetailViewObject{" +
                "sid=" + sid +
                ", upid=" + upid +
                ", shareUser='" + shareUser + '\'' +
                ", fileName='" + fileName + '\'' +
                ", shareDate=" + shareDate +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", ranks=" + ranks +
                ", status=" + status +
                '}';
    }
}
