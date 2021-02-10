package com.example.college.pojo;

import java.util.Date;

public class History {
    private String id;
    private String identity;
    private String operate;
    private Date thisdate;

    @Override
    public String toString() {
        return "History{" +
                "id='" + id + '\'' +
                ", identity='" + identity + '\'' +
                ", operate='" + operate + '\'' +
                ", thisdate=" + thisdate +
                '}';
    }

    public History(){

    }
    public History(String id, String identity, String operate, Date thisdate) {
        this.id = id;
        this.identity = identity;
        this.operate = operate;
        this.thisdate = thisdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Date getThisdate() {
        return thisdate;
    }

    public void setThisdate(Date thisdate) {
        this.thisdate = thisdate;
    }
}
