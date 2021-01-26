package com.example.college.mapper;

import com.example.college.pojo.Leave_stu;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface Leave_stuMapper {
    public List<Leave_stu> findByID(String id);
    public void insertLeave(String id, String username, String location, String reason, Date now_time,Date end_time,String state);
}
