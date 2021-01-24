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
    public List<Student> selectRelative(String major,String grade,String number);
    public List<Student> selectByRelative(String major,String grade,String number,String sex,String username,String stu_id);
}
