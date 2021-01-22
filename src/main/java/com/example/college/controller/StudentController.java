package com.example.college.controller;

import com.example.college.mapper.ClockMapper;
import com.example.college.mapper.StudentMapper;
import com.example.college.mapper.TempleteMapper;
import com.example.college.pojo.Student;
import com.example.college.pojo.Templete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TempleteMapper templeteMapper;
    @Autowired
    ClockMapper clockMapper;
    @GetMapping("/backLogin")
    public String backLogin(){
        templeteMapper.delete();
        return "login";
    }
    /*实现打卡，在当天12：00-23：30之间可以打卡*/
    @GetMapping("/clock")
    public String clock(Map<String,Object> map) throws ParseException {
        String id=templeteMapper.find().getId();
        Date date=new Date();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        Date dateNow=format1.parse(format1.format(date));//转换之后是String类型
        String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
        String dateEndS=format2.format(date)+" 23:30:00";
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
            studentMapper.updateState(id,"已打卡");
            clockMapper.insertID(id);
        }else if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
            map.put("error","不在规定打卡时间内");
        }
        List<Student> students=studentMapper.findByLocation(studentMapper.findById(id).getLocation());
        map.put("students",students);
        map.put("id",id);
        return "student/studentMain";
    }
}
