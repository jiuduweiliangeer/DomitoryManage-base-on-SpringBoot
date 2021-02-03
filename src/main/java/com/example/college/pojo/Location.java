package com.example.college.pojo;

public class Location {
    private String building;
    private String floor;
    private String is_home;
    private String state;
    public Location(){

    }

    public Location(String building, String floor, String is_home, String state) {
        this.building = building;
        this.floor = floor;
        this.is_home = is_home;
        this.state = state;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getIs_home() {
        return is_home;
    }

    public void setIs_home(String is_home) {
        this.is_home = is_home;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
