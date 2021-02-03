package com.example.college.pojo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

public class Student {
    private String stu_id;
    private String username;
    private String password;
    private String sex;
    private String grade;
    private String number;
    private String major;
    private String state;
    private String location;
    private String email;

    public Student(){

    }
    public Student(String stu_id, String username, String password, String sex, String grade, String number, String major, String state, String location, String email) {
        this.stu_id = stu_id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.grade = grade;
        this.number = number;
        this.major = major;
        this.state = state;
        this.location = location;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stu_id='" + stu_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", grade='" + grade + '\'' +
                ", number='" + number + '\'' +
                ", major='" + major + '\'' +
                ", state='" + state + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
