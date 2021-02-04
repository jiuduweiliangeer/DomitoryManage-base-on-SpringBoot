package com.example.college.pojo;

import java.util.Date;

public class Absence {
    private String id;
    private String name;
    private Date date_absence;
    private String location;
    private String state;

    @Override
    public String toString() {
        return "Absence{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date_absence=" + date_absence +
                ", location='" + location + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public Absence(){

    }
    public Absence(String id, String name, Date date_absence, String location, String state) {
        this.id = id;
        this.name = name;
        this.date_absence = date_absence;
        this.location = location;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate_absence() {
        return date_absence;
    }

    public void setDate_absence(Date date_absence) {
        this.date_absence = date_absence;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
