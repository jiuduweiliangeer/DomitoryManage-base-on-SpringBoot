package com.example.college.pojo;

public class Templete {
    private String id;
    private Templete(){

    }
    public Templete(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
