package com.lwzw.cloud.bean;

public class Relation {
    private Integer relaid;

    private Integer fromuid;

    private Integer touid;

    public Integer getRelaid() {
        return relaid;
    }

    public void setRelaid(Integer relaid) {
        this.relaid = relaid;
    }

    public Integer getFromuid() {
        return fromuid;
    }

    public void setFromuid(Integer fromuid) {
        this.fromuid = fromuid;
    }

    public Integer getTouid() {
        return touid;
    }

    public void setTouid(Integer touid) {
        this.touid = touid;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "relaid=" + relaid +
                ", fromuid=" + fromuid +
                ", touid=" + touid +
                '}';
    }
}