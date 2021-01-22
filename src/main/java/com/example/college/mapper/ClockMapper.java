package com.example.college.mapper;

import com.example.college.pojo.Clock;
import org.springframework.stereotype.Repository;

@Repository
public interface ClockMapper {
    public Clock find(String id);
    public void insertID(String id);
    public void delete();
}
