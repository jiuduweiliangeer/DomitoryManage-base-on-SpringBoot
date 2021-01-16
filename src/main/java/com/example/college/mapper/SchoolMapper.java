package com.example.college.mapper;

import com.example.college.pojo.School;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolMapper {
    public School findById(String id);
}
