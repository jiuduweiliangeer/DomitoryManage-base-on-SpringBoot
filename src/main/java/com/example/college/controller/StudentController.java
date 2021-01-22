package com.example.college.controller;

import com.example.college.mapper.ClockMapper;
import com.example.college.mapper.StudentMapper;
import com.example.college.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    ClockMapper clockMapper;
    @GetMapping("/backLogin")
    public String backLogin(){
        return "login";
    }
    /*实现打卡，在当天12：00-23：30之间可以打卡*/

    @GetMapping("/clock/{id}")
    public String clock(Map<String,Object> map,
                        @PathVariable String id) throws ParseException {
        Date date=new Date();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
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
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        Date dateNow=format1.parse(format1.format(date));//转换之后是String类型
        String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
        String dateEndS=format2.format(date)+" 23:30:00";
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        Student student=studentMapper.findById(id);
        String state=student.getState();
        try{
            if (clockMapper.find(id).getDatethis().before(dateBegin)){
                clockMapper.delete();//防止第二天有前一天的数据存在，需要清空表，如果本身是当前数据是空表直接catch到下面
            }
        }catch (Exception e){
            if (dateNow.before(dateBegin)||dateNow.after(dateEnd)){
                clockMapper.delete();//在当天时间中清空表
            }
        }
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
        map.put("stu",student);
        return "student/studentMain";
    }
    /*无数据，测试前端页面*/
    @GetMapping("/selectRelative/{id}")
    public String toRelative(@PathVariable("id") String id,
                             Map<String,Object> map){
        List<Student> students=studentMapper.findByLocation(studentMapper.findById(id).getLocation());
        map.put("students",students);
        map.put("stu",studentMapper.findById(id));
        return "student/studentRelative";
    }
}