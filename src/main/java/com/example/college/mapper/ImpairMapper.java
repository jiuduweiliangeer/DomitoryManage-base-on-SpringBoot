package com.example.college.mapper;

import com.example.college.pojo.Impair;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImpairMapper {
    public List<Impair> findByID(String id);
    public void insertImpair(String id, String location, String thisname, Date thistime,String content,String is_deal,String username);

}
