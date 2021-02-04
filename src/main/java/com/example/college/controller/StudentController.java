package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.Impair;
import com.example.college.pojo.Leave_stu;
import com.example.college.pojo.Student;
import com.example.college.sendemail.Demo;
import com.example.college.sendemail.Send;
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
    @Autowired
    ApartmentMapper apartmentMapper;
    @Autowired
    AbsenceMapper absenceMapper;
    @Autowired
    Demo demo;
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
        String dateEndS=format2.format(date)+" 16:30:00";
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
                absenceMapper.insertAbsence(id,student.getUsername(),dateNow,student.getLocation(),"未上报");
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
        /*
        * 此处还需要做一个判定，判断开始时间和结束时间的先后，暂未做，前端后续工程完工后，进行补充
        * 前端的数据传输过来应该是yyyy/MM/DDTHH:mm,String类型，所以使用String类型下的replace来修改成String类型的设定格式的Date模板，然后进行转换
        * */
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
    /*进入个人信息*/
    @GetMapping("/personal/{id}")
    public String toPersonal(@PathVariable("id") String id,
                             Map<String,Object> map){
        map.put("stu",studentMapper.findById(id));
        return "student/personalMessage";
    }
    /*进入修改密码界面*/
    @GetMapping("/personal/reset/{id}")
    public String toReset(@PathVariable("id") String id,
                          Map<String,Object> map){
        map.put("stu",studentMapper.findById(id));
        return "student/resetPassword";
    }
    /*修改密码*/
    @PostMapping("/personal/reset/{id}")
    public String resetPassword(@PathVariable("id") String id,
                                @RequestParam("old_password") String old_password,
                                @RequestParam("new_password") String new_password,
                                Map<String,Object> map){
        String s=null;
        Student student=studentMapper.findById(id);
        map.put("stu",student);
        if (student.getPassword().equals(old_password)){
            studentMapper.setNewPassword(id,new_password);
            s="student/personalMessage";
        }else {
            map.put("error","原密码不正确");
            s="student/resetPassword";
        }
        return s;
    }
    /*进入绑定邮箱界面*/
    /*
    * 几种情况
    *   1.当用户已经绑定邮箱后，应该显示错误信息为“已绑定邮箱”（此为初步设定，后续可能会改成修改邮箱）
    *   2.当用户没有绑定邮箱时，应该直接进入绑定邮箱界面
    *       在绑定邮箱界面里，需要考虑的错误情况：
    *           检查Apa和Stu表中的邮箱数据，查看是否有相同的邮箱数据，如果有相同的邮箱，则返回错误数据”当前邮箱已被绑定“
    *           当检测通过后，进入确认验证码阶段，在这个界面应该有重复提交的机会（在登录的时候因为对POST和GET理解不够没有实现，后续整理
    *           时应该进行修改）
    *           绑定成功后返回主界面
    * */
    @GetMapping("/personal/bind/{id}")
    public String toBind(@PathVariable("id") String id,
                         Map<String,Object> map){
        Student student=studentMapper.findById(id);
        map.put("stu",student);
        String s=null;
        String t=student.getEmail();
        if(t==null||t.trim().equals("")){
            s="student/bindEmail";
        }else {
            map.put("error","已绑定邮箱");
            s="student/personalMessage";
        }
        return s;
    }
    @PostMapping("/personal/bind/{id}")
    public String BindEmail_1(@PathVariable("id") String id,
                              @RequestParam("email") String email,
                              Map<String,Object> map){
        String s=null;
        Student student=studentMapper.findById(id);
        map.put("stu",student);
        if (studentMapper.findEmail(email)==null&&apartmentMapper.findEmail(email)==null){
            String code=demo.email(email);
            map.put("code",code);
            map.put("email",email);
            s="student/bindEmail_2";
        }else {
            map.put("error","当前邮箱已被绑定");
            s="student/bindEmail";
        }
        return s;
    }
    /*确认验证码*/
    @PostMapping("/personal/bind/{id}/{code}/{email}")
    public String BindEmail_2(@PathVariable("id") String id,
                              @PathVariable("code") String code,
                              @PathVariable("email") String email,
                              @RequestParam("email_code") String email_code,
                              Map<String,Object> map){
        String s=null;
        Student student=studentMapper.findById(id);
        map.put("stu",student);
        if (email_code.equals(code)){
            studentMapper.setEmail(id,email);
            s="student/personalMessage";
        }else {
            map.put("error","您输入的验证码不正确");
            map.put("code",code);
            map.put("email",email);
            s="student/bindEmail_2";
        }
        return s;
    }

}