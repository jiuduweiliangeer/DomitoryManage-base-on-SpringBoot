package com.example.college.pojo;

import java.util.Date;

public class Clock {
    private String id;
    private Date datethis;
    private Clock(){

    }

    public Clock(String id, Date datethis) {
        this.id = id;
        this.datethis = datethis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDatethis() {
        return datethis;
    }

    public void setDatethis(Date datethis) {
        this.datethis = datethis;
    }
}
