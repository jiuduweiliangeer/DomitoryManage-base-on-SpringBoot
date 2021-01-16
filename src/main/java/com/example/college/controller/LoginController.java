package com.example.college.controller;

import com.example.college.mapper.ApartmentMapper;
import com.example.college.mapper.SchoolMapper;
import com.example.college.mapper.StudentMapper;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.School;
import com.example.college.pojo.Student;
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
    Demo demo;
    /*设置初始页面*/
    @GetMapping("/")
    public String toLogin(){
        return "login";
    }
    /*登录*/
    @RequestMapping("/main")
    public String toMain(@RequestParam("id") String id,
                                @RequestParam("password") String password,
                                @RequestParam("optionsRadios") Integer optionsRadios,
                                Map<String,Object> map){
        String s=null;
        Student student=studentMapper.findById(id);
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(id);
        switch (optionsRadios){
            case 1:
                if (student!=null){
                    String password1=student.getPassword();
                    if (password.equals(password1)){
                        map.put("id",id);
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
            case 2:
                if (apartment!=null){
                    String password1=apartment.getPassword();
                    if (password.equals(password1)){
                        map.put("id",id);
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
    @PostMapping("/confirm/{id}/{code}")
    public String ConfirmCode(@PathVariable("id") String id,
                              @PathVariable("code") String code,
                              @RequestParam("confirmCode") String confirmCode,
                              @RequestParam("newPassword") String newPassword,
                              Map<String,Object> map,
                              HttpServletResponse response) throws IOException {
        String s=null;
        if (confirmCode.equals(code)){
            studentMapper.setNewPassword(id,newPassword);
            map.put("success","修改成功");
            s="login";
        }else {
            map.put("error","验证失败");
            s="login";
        }
        return s;
    }
    @RequestMapping("testconfirm")
    public String testconfirm(){
        return "testLogin";
    }
}
