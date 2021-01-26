package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Impair;
import com.example.college.pojo.Leave_stu;
import com.example.college.pojo.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Controller
public class StudentController {
    Logger logger= LoggerFactory.getLogger(getClass());
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ClockMapper clockMapper;
    @Autowired
    ImpairMapper impairMapper;
    @Autowired
    Leave_stuMapper leaveStuMapper;
    @Autowired
    SuggestMapper suggestMapper;
    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
    @GetMapping("/backLogin")
    public String backLogin(){
        return "login";
    }
    /*实现打卡，在当天12：00-23：30之间可以打卡*/

    @GetMapping("/clock/{id}")
    public String clock(Map<String,Object> map,
                        @PathVariable String id) throws ParseException {
        Date date=new Date();
        Date dateNow=format1.parse(format1.format(date));//转换之后是String类型
        String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
        String dateEndS=format2.format(date)+" 23:30:00";
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
            if (studentMapper.findById(id).getState().equals("在校（未打卡）")){
                studentMapper.updateState(id,"已打卡");
                clockMapper.insertAll(id,dateNow);
            }else {
                map.put("error","请不要重复打卡");
            }
            //此处应上传至历史记录表，暂未完成，先做主体功能，附加表后续接入,缺勤情况不会在这个时间段内显示，所以可以直接修改，此处代码在LoginController
        }else if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
            map.put("error","不在规定打卡时间内");
        }
        List<Student> students=studentMapper.findByLocation(studentMapper.findById(id).getLocation());
        map.put("students",students);
        map.put("stu",studentMapper.findById(id));
        return "student/studentMain";
    }
    /*本宿舍情况,类似登录，使用公共页面，在同一个登录id下id已经确定，无需重定向，直接通过公共页面定义url进入Controller即可*/
    @GetMapping("/selectAll/{id}")
    public String studentMain(@PathVariable("id") String id,
                              Map<String,Object> map) throws ParseException {
        /*每次进入都需要检查状态，此处复制登录*/
        Date date=new Date();
        /*判定时间,12:00-12:00+1为一个周期*/
        Date dateNow=format1.parse(format1.format(date));//转换之后是String类型
        String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
        String dateEndS=format2.format(date)+" 23:30:00";
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        Student student=studentMapper.findById(id);
        String state=student.getState();
        if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
            logger.info("清空打卡表");
            clockMapper.delete();//在当天时间中清空表
        }
        //判定当前状态，根据时间来确定状态并进行修改，修改之后再进入主界面
        //还应该根据是否请假来进行判断，这一段等后面基本完成再补充
        if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
            if (state.equals("在校（未打卡）")){
                studentMapper.updateState(id,"缺勤");
                //此处应该上报至缺勤表(缺勤人，缺勤时间)，暂时未做，最后加上
            }
        }else if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
            try{
                if (clockMapper.find(id).getDatethis().before(dateBegin)){
                    logger.info("调用二重判断删除打卡表");
                    clockMapper.deleteID(id);//防止第二天有前一天的数据存在，需要清空表,如果本身已经为空则不影响后续操作
                }
            }catch (Exception e){
                logger.warn("当前打卡表数据为空");
            }
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
        map.put("stu",student);
        return "student/studentMain";
    }
    /*进入宿舍相关页面使用GET*/
    @GetMapping("/selectRelative/{id}")
    public String toRelative(@PathVariable("id") String id,
                             Map<String,Object> map){
        Student stu=studentMapper.findById(id);
        List<Student> students=studentMapper.selectRelative(stu.getMajor(),stu.getGrade(),stu.getNumber());
        map.put("students",students);
        map.put("stu",stu);
        return "student/studentRelative";
    }
    /*查找相关属性，提交form表单，使用POST请求，url不变*/
    @PostMapping("/selectRelative/{id}")
    public String selectRelative(@PathVariable("id") String id,
                                 @RequestParam("sex") String sex,
                                 @RequestParam("username") String username,
                                 @RequestParam("stu_id") String stu_id,
                                 Map<String,Object> map){
        Student stu=studentMapper.findById(id);
        if (sex==""){
            sex=null;
        }
        if (username==""){
            username=null;
        }
        if (stu_id==""){
            stu_id=null;
        }
        List<Student> students=studentMapper.selectByRelative(stu.getMajor(),stu.getGrade(),stu.getNumber(),sex,username,stu_id);
        map.put("students",students);
        map.put("stu",stu);
        return "student/studentRelative";
    }
    /*进入申报页面*/
    @GetMapping("/impair/{id}")
    public String toImpair(@PathVariable("id") String id,
                           Map<String,Object> map) {
        List<Impair> impairs=null;
        try {
            impairs=impairMapper.findByID(id);
        }catch (Exception e){
            logger.warn("当前申报表为空");
        }
        map.put("impairs",impairs);
        map.put("stu",studentMapper.findById(id));
        return "student/studentImpair";
    }
    /*提交申报信息*/
    @PostMapping("/impair/{id}")
    public String SelectImpair(@PathVariable("id") String id,
                               @RequestParam("thisname") String thisname,
                               @RequestParam("content") String content,
                               Map<String,Object> map) throws ParseException {
        Date date=new Date();
        Student student=studentMapper.findById(id);
        System.out.println(format1.parse(format1.format(date)));
        impairMapper.insertImpair(id, student.getLocation(),thisname,format1.parse(format1.format(date)),content,"待处理",student.getUsername());
        List<Impair> impairs=impairMapper.findByID(id);
        map.put("impairs",impairs);
        map.put("stu",student);
        return "student/studentImpair";

    }
    /*进入请假页面*/
    @GetMapping("/leave/{id}")
    public String toLeave(@PathVariable("id") String id,
                          Map<String,Object> map){
        List<Leave_stu> leaves=null;
        try {
            leaves= leaveStuMapper.findByID(id);
        }catch (Exception e){
            logger.warn("当前请假表为空");
        }
        map.put("leaves",leaves);
        map.put("stu",studentMapper.findById(id));
        return "student/studentLeave";
    }
    //*提交请假信息*//*
    @PostMapping("/leave/{id}")
    public String SelectLeave(@PathVariable("id") String id,
                              @RequestParam("now_time") String now_time,
                              @RequestParam("end_time") String end_time,
                              @RequestParam("reason") String reason,
                              Map<String,Object> map) throws ParseException {
        Student student=studentMapper.findById(id);
        Date date1= format1.parse(now_time.replace("T"," ")+":00");
        Date date2= format1.parse(end_time.replace("T"," ")+":00");
        leaveStuMapper.insertLeave(id,student.getUsername(),student.getLocation(),reason,date1,date2,"待处理");
        List<Leave_stu> leaves= leaveStuMapper.findByID(id);
        map.put("leaves",leaves);
        map.put("stu",student);
        return "student/studentLeave";
    }
    /*进入建议页面*/
    @GetMapping("/suggest/{id}")
    public String toSuggest(@PathVariable("id") String id,
                            Map<String,Object> map){
        map.put("stu",studentMapper.findById(id));
        return "student/studentSuggest";
    }
    /*提交建议*/
    @PostMapping("/suggest/{id}")
    public String SelectSuggest(@PathVariable("id") String id,
                                @RequestParam("content") String content,
                                Map<String,Object> map) throws ParseException {
        Date date=new Date();
        Student student=studentMapper.findById(id);
        suggestMapper.insertSuggest(id,student.getUsername(),student.getLocation(),format1.parse(format1.format(date)),content);
        map.put("stu",student);
        return "student/studentSuggest";
    }
}