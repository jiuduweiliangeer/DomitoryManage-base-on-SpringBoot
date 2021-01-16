package com.example.college.pojo;

public class School {
    private String sch_id;
    private String sch_username;
    private String sch_password;
    public School(){

    }
    public School(String sch_id, String sch_username, String sch_password) {
        this.sch_id = sch_id;
        this.sch_username = sch_username;
        this.sch_password = sch_password;
    }

    @Override
    public String toString() {
        return "School{" +
                "sch_id='" + sch_id + '\'' +
                ", sch_username='" + sch_username + '\'' +
                ", sch_password='" + sch_password + '\'' +
                '}';
    }

    public String getSch_id() {
        return sch_id;
    }

    public void setSch_id(String sch_id) {
        this.sch_id = sch_id;
    }

    public String getSch_username() {
        return sch_username;
    }

    public void setSch_username(String sch_username) {
        this.sch_username = sch_username;
    }

    public String getSch_password() {
        return sch_password;
    }

    public void setSch_password(String sch_password) {
        this.sch_password = sch_password;
    }
}
