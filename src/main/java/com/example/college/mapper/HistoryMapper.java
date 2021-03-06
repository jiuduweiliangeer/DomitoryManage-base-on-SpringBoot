package com.example.college.mapper;

import com.example.college.pojo.History;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryMapper {
    public List<History> findByID(String id);
    public List<History> selectByApartment(String id, String operate);
    /*暂时没用到*/
    public void insertHistory(String id, String identity, String operate, Date thisdate);
    public void deleteHistory(String id, Date thisdate);
}
