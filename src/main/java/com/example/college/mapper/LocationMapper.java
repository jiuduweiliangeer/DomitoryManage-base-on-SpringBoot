package com.example.college.mapper;

import com.example.college.pojo.Location;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationMapper {
    public List<Location> findByBuilding(String building);
    public List<Location> SelectLocation(String building, String is_home, String state, String floor);
    public void UpdateIs_home(String building, String floor, String is_home);
    public void UpdateState(String building, String floor, String state);
}
