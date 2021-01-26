package com.example.college.pojo;

import java.util.Date;

public class Suggest {
    private String id;
    private String username;
    private String location;
    private Date thistime;
    private String content;
    private Suggest(){

    }
    public Suggest(String id, String username, String location, Date thistime, String content) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.thistime = thistime;
        this.content = content;
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
}
