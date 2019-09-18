package com.lwzw.cloud.bean.viewObject;

import java.util.Date;

/**
 * 公共分享条目ViewObject
 */
public class CommonShareViewObject {
    private Integer sid;//分享条目id
    private String nickname;//分享人昵称
    private String fileName;//文件名
    private Integer ranks;//下载等级
    private Date shareDate;//分享日期

    public CommonShareViewObject(Integer sid, String nickname, String fileName, Integer ranks, Date shareDate) {
        this.sid = sid;
        this.nickname = nickname;
        this.fileName = fileName;
        this.ranks = ranks;
        this.shareDate = shareDate;
    }

    public CommonShareViewObject() {
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getRanks() {
        return ranks;
    }

    public void setRanks(Integer ranks) {
        this.ranks = ranks;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    @Override
    public String toString() {
        return "CommonShareViewObject{" +
                "sid=" + sid +
                ", nickname=" + nickname +
                ", fileName='" + fileName + '\'' +
                ", ranks=" + ranks +
                ", shareDate=" + shareDate +
                '}';
    }
}
