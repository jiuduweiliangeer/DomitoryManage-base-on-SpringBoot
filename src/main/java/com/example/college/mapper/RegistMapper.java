package com.example.college.mapper;

import com.example.college.pojo.Regist;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistMapper {
    //查询所有的登记记录
    public List<Regist> queryRegists(String registrar);
    //删除外来登记记录
    public void deleteRegist(int id);
    //进行登记记录
    public void insertRegist(String name, String reason, String in_time, String out_time, String phone, String registrar);
    //通过名字查询登记记录
    public List<Regist> queryByLike(String name, String reason, String in_time, String registrar);

}
