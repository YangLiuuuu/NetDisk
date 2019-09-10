package com.lwzw.cloud.bean.viewObject;

import java.util.Date;

public class ShareMessageViewObject {
    private Integer sid;//分享表项id
    private Integer fid;//文件id
    private String fileName;//文件名
    private Double fileSize;//文件大小
    private String shareUsername;
    private Date shareDate;

    public ShareMessageViewObject() {
    }

    public ShareMessageViewObject(Integer fid, String fileName, Double fileSize, String shareUsername, Date shareDate,Integer sid) {
        this.fid = fid;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.shareUsername = shareUsername;
        this.shareDate = shareDate;
        this.sid = sid;
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

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getShareUsername() {
        return shareUsername;
    }

    public void setShareUsername(String shareUsername) {
        this.shareUsername = shareUsername;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "ShareMessageViewObject{" +
                "fid=" + fid +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", shareUsername='" + shareUsername + '\'' +
                ", shareDate=" + shareDate +
                ", sid=" + sid +
                '}';
    }
}
