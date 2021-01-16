package com.example.college.mapper;

import com.example.college.pojo.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper {
    public Student findById(String id);
    public void setNewPassword(String id,String password);
}
