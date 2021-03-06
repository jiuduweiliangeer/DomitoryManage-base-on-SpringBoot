package com.example.college.mapper;

import com.example.college.pojo.Impair;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImpairMapper {
    public List<Impair> findByID(String id);
    public void insertImpair(String id, String location, String thisname, Date thistime, String content, String is_deal, String username);
    public List<Impair> selectByBuildingLike(String building);
    /*修改处理状态*/
    public void dealState(String is_deal, String id, Date thistime);
    /*管理员查询申报表*/
    public List<Impair> selectByApartment(String building, String is_deal, String location);
}
