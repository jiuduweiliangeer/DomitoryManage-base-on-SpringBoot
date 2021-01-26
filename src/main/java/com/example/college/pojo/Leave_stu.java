package com.example.college.pojo;

import java.util.Date;

public class Leave_stu {
    private String id;
    private String username;
    private String location;
    private String reason;
    private Date now_time;
    private Date end_time;
    private String state;
    public Leave_stu(){

    }

    @Override
    public String toString() {
        return "Leave{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", location='" + location + '\'' +
                ", reason='" + reason + '\'' +
                ", now_time=" + now_time +
                ", end_time=" + end_time +
                ", state='" + state + '\'' +
                '}';
    }

    public Leave_stu(String id, String username, String location, String reason, Date now_time, Date end_time, String state) {
        this.id = id;
        this.username = username;
        this.location = location;
        this.reason = reason;
        this.now_time = now_time;
        this.end_time = end_time;
        this.state = state;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getNow_time() {
        return now_time;
    }

    public void setNow_time(Date now_time) {
        this.now_time = now_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
