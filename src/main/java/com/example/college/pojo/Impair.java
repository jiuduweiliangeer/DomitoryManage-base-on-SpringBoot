package com.example.college.pojo;

import java.util.Date;

public class Impair {
    private String id;
    private String username;
    private String location;
    private String thisname;
    private Date thistime;
    private String content;
    private String is_deal;
    public Impair(){

    }
    public Impair(String id, String username, String location, String thisname, Date thistime, String content, String is_deal) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.thisname = thisname;
        this.thistime = thistime;
        this.content = content;
        this.is_deal = is_deal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getThisname() {
        return thisname;
    }

    public void setThisname(String thisname) {
        this.thisname = thisname;
    }

    public Date getThistime() {
        return thistime;
    }

    public void setThistime(Date thistime) {
        this.thistime = thistime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_deal() {
        return is_deal;
    }

    public void setIs_deal(String is_deal) {
        this.is_deal = is_deal;
    }
}
