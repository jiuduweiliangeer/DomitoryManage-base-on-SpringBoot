package com.example.college.mapper;

import com.example.college.pojo.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {
    /*管理员插入学生信息*/
    public void InsertStudent(String stu_id,String username,String password,String sex,String grade,String number,String major,String state,String location);
    /*通过id查找学生，多处使用*/
    public Student findById(String id);
    /*查询所有学生，主要用于学校管理员*/
    public List<Student> findAll();
    /*删除学生，用于管理员管理学生*/
    public void deleteStudent(String id);
    /*通过email查找学生，用于绑定邮箱时查询内容*/
    public Student findEmail(String email);
    /*设置邮箱*/
    public void setEmail(String id,String email);
    /*设置新密码*/
    public void setNewPassword(String id,String password);
    /*通过宿舍进行查询，多处使用*/
    public List<Student> findByLocation(String location);
    /*修改学生状态，用于管理员*/
    public void updateState(String id,String state);
    /*查询相关宿舍，用于学生进入相关宿舍页面*/
    public List<Student> selectRelative(String major,String grade,String number);
    /*模糊查询，学生搜索*/
    public List<Student> selectByRelative(String major,String grade,String number,String sex,String username,String stu_id);
    /*模糊查询，用于管理员进入学生信息页面*/
    public List<Student> selectByBuildingLike(String building);
    /*模糊查询，用于管理员在学生信息页面查询*/
    public List<Student> selectByApartment(String building,String grade,String location,String stu_id);
    /*模糊查询，用于distributionDetails页面进行:姓名 年级 班级 寝室号 专业 的查询*/
    public List<Student> selectByFloor(String grade,String number,String location,String username,String major);
}
