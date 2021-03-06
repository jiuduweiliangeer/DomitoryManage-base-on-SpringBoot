package com.example.college.mapper;

import com.example.college.pojo.Leave_stu;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface Leave_stuMapper {
    public List<Leave_stu> findByID(String id);
    public void insertLeave(String id, String username, String location, String reason, Date now_time, Date end_time, String state);
    public List<Leave_stu> selectByBuildingLike(String building);
    public void updateState(String id, String state);
    public boolean updateLeaveState(String id, String state);
    public List<Leave_stu> selectByApartment(String building, String state, String id, String location);

    public List<Leave_stu> selectLeaves(int page, int limit);
    public int selectTotal();
    public List<Leave_stu> schLeaves(String state, String id, String location, String building, int page, int limit);
    public int selectCounts(String building);
}
