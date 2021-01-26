package com.example.college.mapper;

import com.example.college.pojo.Suggest;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SuggestMapper {
    public List<Suggest> findAll();
    public void insertSuggest(String id, String username, String location, Date thistime, String content);
}
