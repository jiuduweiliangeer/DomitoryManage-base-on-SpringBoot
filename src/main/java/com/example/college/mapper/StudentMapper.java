package com.example.college.mapper;

import com.example.college.pojo.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {
    public Student findById(String id);
    public void setNewPassword(String id,String password);
    public List<Student> findByLocation(String location);
    public void updateState(String id,String state);
}
