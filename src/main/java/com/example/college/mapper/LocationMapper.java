package com.example.college.mapper;

import com.example.college.pojo.Location;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationMapper {
    public List<Location> findByBuilding(String building);
}
