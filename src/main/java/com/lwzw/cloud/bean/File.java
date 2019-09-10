package com.lwzw.cloud.bean;

import java.util.Date;

public class File {
    private Integer fid;

    private Integer upid;

    private String fname;

    private String url;

    private Double size;

    private Date date;

    public File() {
    }

    public File(Integer upid, String fname, String url, Double size, Date date) {
        this.upid = upid;
        this.fname = fname;
        this.url = url;
        this.size = size;
        this.date = date;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getUpid() {
        return upid;
    }

    public void setUpid(Integer upid) {
        this.upid = upid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "File{" +
                "fid=" + fid +
                ", upid=" + upid +
                ", fname='" + fname + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                ", date=" + date +
                '}';
    }
}