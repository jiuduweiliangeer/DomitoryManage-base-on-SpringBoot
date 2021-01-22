package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.School;
import com.example.college.pojo.Student;
import com.example.college.sendemail.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        try{
            Date date1=clockMapper.find(id).getDatethis();
            if (clockMapper.find(id).getDatethis().before(dateBegin)){
                clockMapper.delete();//防止第二天有前一天的数据存在，需要清空表，如果本身是当前数据是空表直接catch到下面,比第二天开始时间还要早的打卡数据直接删除
            }//高优先级
        }catch (Exception e){
            if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
                clockMapper.delete();//在当天时间中清空表,也就是在规定时间内清空其他id的表，此处可以没有，但为了减少系统运行冗余，还是加上
            }
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
                                //记录：本系统设定的一天时间为前一天12：00-后一天12：00，也就是说，在12：00前记录的缺勤记录和前一天23：30之后记录的缺勤记录为同一天
                            }
                        }else if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
                            if (clockMapper.find(id)==null){
                                studentMapper.updateState(id,"在校（未打卡）");
                                /*、
                                * 如何判断当天是否已经打卡？通过查询打卡表，打卡表当前用户如果存在数据，则不修改，如果不存在，则修改，
                                * 打卡表每天23：30分应该清空一次（主要为了实现登陆查询改状态）
                                * */
                                /*
                                * 此处有BUG，如果一个学生从前一天缺勤拖到了后一天打卡，则无法上传至缺勤表，因此，给宿舍管理员一个刷新权限，
                                * 在某个时间前，宿舍管理员需要进行数据刷新，数据刷新所进行的操作为，遍历整栋宿舍到时未打卡的学生，将他们的
                                * id存储在一个数组中，遍历数组，做一个循环，修改学生状态并上传至缺勤表，展示的时候一般是用不到这里的，但是
                                * 作为一个BUG，还是得想办法解决，因为使用的SpringBoot是通过Controller运行业务，如果不进入这个Controller，
                                * 就无法运行，所以会导致这个BUG，目前能想到的最佳解决办法就是管理员手动刷新
                                *
                                * */
                            }
                        }
                        List<Student> students=studentMapper.findByLocation(student.getLocation());
                        map.put("students",students);
                        map.put("stu",student);
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
