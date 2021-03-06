package com.example.college.mapper;

import com.example.college.pojo.Absence;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AbsenceMapper {
    /*插入缺勤信息，用于缺勤相关以及宿舍管理员确认缺勤情况*/
    public void insertAbsence(String id, String name, Date date_absence, String location, String state);
    /*模糊查询，用于管理员的缺勤信息页面*/
    public List<Absence> selectByBuildingLike(String building);
    /*修改缺勤表状态，用于管理员上报缺勤情况*/
    public void updateAbsenceState(String state, String building);
    /*管理员查询缺勤相关信息*/
    public List<Absence> selectAbsenceLike(String building, String state, String id);
}
