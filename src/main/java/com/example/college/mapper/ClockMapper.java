package com.example.college.mapper;

import com.example.college.pojo.Clock;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ClockMapper {
    public Clock find(String id);
    public void insertAll(String id, Date datethis);
    public void delete();
}
