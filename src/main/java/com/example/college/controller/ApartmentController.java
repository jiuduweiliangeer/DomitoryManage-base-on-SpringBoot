package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.Location;
import com.example.college.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ApartmentController {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    Leave_stuMapper leave_stuMapper;
    @Autowired
    SuggestMapper suggestMapper;
    @Autowired
    ImpairMapper impairMapper;
    @Autowired
    ApartmentMapper apartmentMapper;
    @Autowired
    LocationMapper locationMapper;
    /*进入主页面*/
    @GetMapping("/apaMain/{id}")
    public String toApaMain(@PathVariable("id") String id,
                            Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("locations",locations);
        return "apaAdmin/apaMain";
    }
    /*模糊查询，进入相关楼栋学生信息*/
    @GetMapping("/apaStudentMessage/{id}")
    public String toStudentMessage(@PathVariable("id") String id,
                                   Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        String building=apartment.getApartment()+"-";
        List<Student> students=studentMapper.selectByBuildingLike(building);
        map.put("apa",apartment);
        map.put("students",students);
        return "apaAdmin/studentMessage";

    }
}
