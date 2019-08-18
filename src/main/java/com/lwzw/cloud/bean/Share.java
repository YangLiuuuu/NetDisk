package com.lwzw.cloud.bean;

public class Share {
    private Integer sid;

    private Integer ufid;

    private Integer touid;

    private Integer fromuid;

    private Boolean status;

    private String code;

    private Boolean isread;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUfid() {
        return ufid;
    }

    public void setUfid(Integer ufid) {
        this.ufid = ufid;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Boolean getIsread() {
        return isread;
    }

    public void setIsread(Boolean isread) {
        this.isread = isread;
    }

    @Override
    public String toString() {
        return "Share{" +
                "sid=" + sid +
                ", ufid=" + ufid +
                ", touid=" + touid +
                ", fromuid=" + fromuid +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", isread=" + isread +
                '}';
    }
}