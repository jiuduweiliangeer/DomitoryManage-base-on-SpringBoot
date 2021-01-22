package com.example.college.pojo;

public class Clock {
    private String id;
    private Clock(){

    }
    public Clock(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
