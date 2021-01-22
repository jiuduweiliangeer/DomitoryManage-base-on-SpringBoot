package com.example.college.mapper;

import com.example.college.pojo.Templete;
import org.springframework.stereotype.Repository;

@Repository
public interface TempleteMapper {
    public Templete find();
    public void delete();
    public void insertID(String id);
}
