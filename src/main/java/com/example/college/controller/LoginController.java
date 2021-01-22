package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.School;
import com.example.college.pojo.Student;
import com.example.college.pojo.Templete;
import com.example.college.sendemail.Demo;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ApartmentMapper apartmentMapper;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    TempleteMapper templeteMapper;
    @Autowired
    ClockMapper clockMapper;
    @Autowired
    Demo demo;
    /*设置初始页面*/
    @GetMapping("/")
    public String toLogin(){
        return "login";
    }
    /*登录，做判定，看当前状态来判定是否缺勤（针对学生）*/
    @RequestMapping("/main")
    public String toMain(@RequestParam("id") String id,
                                @RequestParam("password") String password,
                                @RequestParam("optionsRadios") Integer optionsRadios,
                                Map<String,Object> map) throws ParseException {

        Date date=new Date();
        /*判定时间,12:00-12:00+1为一个周期*/
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        Date dateNow=format1.parse(format1.format(date));//转换之后是String类型
        String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
        String dateEndS=format2.format(date)+" 23:30:00";
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        //每天更新一次打卡表，只有再这个时间段内打卡的才会记录进去
        if (dateNow.after(dateEnd)||dateNow.before(dateBegin)){
                clockMapper.delete();
        }
        String s=null;
        Student student=studentMapper.findById(id);
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(id);
        switch (optionsRadios){
            case 1:
                if (student!=null){
                    String password1=student.getPassword();
                    if (password.equals(password1)){
                        String state=student.getState();
                        //判定当前状态，根据时间来确定状态并进行修改，修改之后再进入主界面
                        //还应该根据是否请假来进行判断，这一段等后面基本完成再补充
                        if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
                            if (state.equals("在校（未打卡）")){
                                studentMapper.updateState(id,"缺勤");
                                //此处应该上报至缺勤表(缺勤人，缺勤时间)，暂时未做，最后加上
                            }
                        }else if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
                            if (clockMapper.find(id)==null){
                                studentMapper.updateState(id,"在校（未打卡）");
                                /*、
                                * 如何判断当天是否已经打卡？通过查询打卡表，打卡表当前用户如果存在数据，则不修改，如果不存在，则修改，
                                * 打卡表每天23：30分应该清空一次（主要为了实现登陆查询改状态）
                                * */
                            }
                        }
                        List<Student> students=studentMapper.findByLocation(student.getLocation());
                        map.put("students",students);
                        map.put("id",student.getStu_id());
                        templeteMapper.insertID(student.getStu_id());
                        s="student/studentMain";
                    }else{
                        map.put("error","您输入的密码错误");
                        s="login";
                    }
            }else {
                    map.put("error","您的账号错误");
                    s="login";
                    }
             break;
            case 2:
                if (apartment!=null){
                    String password1=apartment.getPassword();
                    if (password.equals(password1)){
                        map.put("id",id);
                        //根据前端页面修改
                        s="success";
                    }else{
                        map.put("error","您输入的密码错误");
                        s="login";
                    }
                }else {
                    map.put("error","您的账号错误");
                    s="login";
                }
                break;
            case 3:
                if (school!=null){
                    String password1=school.getSch_password();
                    if (password.equals(password1)){
                        map.put("id",id);
                        //根据前端页面修改
                        s="success";
                    }else{
                        map.put("error","您输入的密码错误");
                        s="login";
                    }
                }else {
                    map.put("error","您的账号错误");
                    s="login";
                }
                break;
        }
        return s;
    }
    /*发邮件，去验证页面*/
    @RequestMapping("/toConfirm")
    public String Confirm(@RequestParam("id") String id,
                            @RequestParam("email") String email,
                            @RequestParam("optionsRadios") Integer optionsRadios,
                            Map<String,Object> map,
                            HttpServletResponse response) throws IOException {
        Student student=studentMapper.findById(id);
        Apartment apartment=apartmentMapper.findById(id);
        String t=null;
        switch (optionsRadios){
            case 1:
                if(student!=null){
                    if(email.equals(student.getEmail())){
                        String s=demo.email(email);
                        map.put("optionsRadios",optionsRadios);
                        map.put("code",s);
                        map.put("id",id);
                        t="confirm";
                    }else {
                       map.put("error","您输入的邮箱错误");
                        t="login";
                    }
                }else {
                   map.put("error","您输入的账号错误");
                   t="login";
                }
                break;
            case 2:
                if(apartment!=null){
                    if(email.equals(apartment.getEmail())){
                        String s=demo.email(email);
                        map.put("optionsRadios",optionsRadios);
                        map.put("code",s);
                        map.put("id",id);
                        s="confirm";
                    }else {
                        map.put("error","您输入的邮箱错误");
                        t="login";
                    }
                }else {
                    map.put("error","您的账号错误");
                    t="login";
                }
                break;
        }
        return t;
    }
    /*确认验证码，修改密码*/
    @PostMapping("/confirm/{id}/{code}/{optionsRadios}")
    public String ConfirmCode(@PathVariable("id") String id,
                              @PathVariable("code") String code,
                              @PathVariable("optionsRadios") Integer optionsRadios,
                              @RequestParam("confirmCode") String confirmCode,
                              @RequestParam("newPassword") String newPassword,
                              Map<String,Object> map,
                              HttpServletResponse response) throws IOException {
        String s=null;
        switch (optionsRadios){
            case 1:
                if (confirmCode.equals(code)){
                    studentMapper.setNewPassword(id,newPassword);
                    map.put("success","修改成功");
                    s="login";
                }else {
                    map.put("error","验证失败");
                    s="login";
                }
                break;
            case 2:
                if (confirmCode.equals(code)){
                    apartmentMapper.setNewPassword(id,newPassword);
                    map.put("success","修改成功");
                    s="login";
                }else {
                    map.put("error","验证失败");
                    s="login";
                }
        }
        return s;
    }
}