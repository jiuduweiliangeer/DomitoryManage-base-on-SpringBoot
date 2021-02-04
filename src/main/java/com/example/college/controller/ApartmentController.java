package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Absence;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.Location;
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
public class ApartmentController {
    Logger logger= LoggerFactory.getLogger(getClass());
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
    @Autowired
    AbsenceMapper absenceMapper;
    Date date=new Date();
    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
    String dateBeginS=format2.format(date)+" 12:00:00";//String 拼接
    String dateEndS=format2.format(date)+" 16:30:00";
    /*进入主页面*/
    @GetMapping("/apaMain/{id}")
    public String toApaMain(@PathVariable("id") String id,
                            Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        /*根据模糊查询出来的本栋学生数据来判断是否有缺勤，如果有则讲学生所在宿舍的宿舍学生情况改为有缺勤*/
        List<Student> students=studentMapper.selectByBuildingLike(apartment.getApartment()+"-");
        try {
            for (int i=0;i<students.size();i++){
                if (students.get(i).getState().equals("缺勤")){
                    String[] split=students.get(i).getLocation().split("-");/*将学生的楼栋和宿舍分离，方便后续查询*/
                    String floor=split[1];/*得到宿舍号*/
                    String is_home="有缺勤";
                    /*进行修改，floor为分割后的学生宿舍号，用宿舍管理员的楼栋号和学生的宿舍号来进行修改和查询*/
                    locationMapper.UpdateIs_home(apartment.getApartment(),floor,is_home);
                }
            }
        }catch (Exception e){
            logger.info("本楼栋没有数据");
        }
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("locations",locations);
        return "apaAdmin/apaMain";
    }
    /*查询宿舍相关*/
    @PostMapping("/apaMain/SelectLocation/{id}")
    public String SelectLocation(@PathVariable("id") String id,
                                 @RequestParam("is_home") String is_home,
                                 @RequestParam("state") String state,
                                 @RequestParam("floor") String floor,
                                 Map<String,Object> map){
        if (is_home==""){
            is_home=null;
        }
        if (state==""){
            state=null;
        }
        if (floor==""){
            floor=null;
        }
        Apartment apartment=apartmentMapper.findById(id);
        String building=apartment.getApartment();
        List<Location> locations=locationMapper.SelectLocation(building,is_home,state,floor);
        map.put("apa",apartment);
        map.put("locations",locations);
        return "apaAdmin/apaMain";
    }
    /*标记宿舍，修改宿舍状态为危险*/
    @GetMapping("/apaMain/sign/{id}/{floor}")
    public String signLocation(@PathVariable("id") String id,
                               @PathVariable("floor") String floor,
                               Map<String,Object> map){
        String state="危险";
        Apartment apartment=apartmentMapper.findById(id);
        locationMapper.UpdateState(apartment.getApartment(),floor,state);
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("locations",locations);
        return "apaAdmin/apaMain";
    }
    /*取消标记，修改宿舍状态为安全，URL的变化通过前台判定宿舍的状态来进行修改，具体操作见前台*/
    @GetMapping("/apaMain/resign/{id}/{floor}")
    public String resignLoctaion(@PathVariable("id") String id,
                                 @PathVariable("floor") String floor,
                                 Map<String,Object> map){
        String state="安全";
        Apartment apartment=apartmentMapper.findById(id);
        locationMapper.UpdateState(apartment.getApartment(),floor,state);
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("locations",locations);
        return "apaAdmin/apaMain";
    }
    /*查看某个宿舍的详细信息*/
    @GetMapping("/apaMain/examine/{id}/{floor}")
    public String examineLoctaion(@PathVariable("id") String id,
                                  @PathVariable("floor") String floor,
                                  Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        String location=apartment.getApartment()+"-"+floor;
        List<Student> students=studentMapper.findByLocation(location);
        map.put("apa",apartment);
        map.put("students",students);
        return "apaAdmin/locationMessage";
    }
    /*模糊查询，进入相关楼栋学生信息*/
    @GetMapping("/apaStudentMessage/{id}")
    public String toStudentMessage(@PathVariable("id") String id,
                                   Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        /*模糊查询，原因：
        *             在location表里使用两个字段来确定宿舍，即楼栋和宿舍号
        *             在student表中直接使用一个宿舍字段来表示楼栋和宿舍，如15-518*/
        String building=apartment.getApartment()+"-";
        List<Student> students=studentMapper.selectByBuildingLike(building);
        map.put("apa",apartment);
        map.put("students",students);
        return "apaAdmin/studentMessage";

    }
    /*查询学生信息*/
    @PostMapping("/apaStudentMessage/select/{id}")
    public String SelectStuMessage(@PathVariable("id") String id,
                                   @RequestParam("grade") String grade,
                                   @RequestParam("location") String location,
                                   @RequestParam("stu_id") String stu_id,
                                   Map<String,Object> map){
        if (grade==""){
            grade=null;
        }
        if (location==""){
            location=null;
        }
        if (stu_id==""){
            stu_id=null;
        }
        Apartment apartment=apartmentMapper.findById(id);
        String building=apartment.getApartment()+"-";
        List<Student> students=studentMapper.selectByApartment(building,grade,location,stu_id);
        map.put("apa",apartment);
        map.put("students",students);
        return "apaAdmin/studentMessage";
    }
    /*进入添加学生页面*/
    @GetMapping("/apaStudentMessage/addStudent/{id}")
    public String toAddStudent(@PathVariable("id") String id,
                               Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        map.put("apa",apartment);
        return "apaAdmin/addStudent";
    }
    /*添加学生并返回学生信息页面*/
    @PostMapping("/apaStudentMessage/addStudent/{id}")
    public String addStudent(@PathVariable("id") String id,
                             @RequestParam("stu_id") String stu_id,
                             @RequestParam("username") String username,
                             @RequestParam("sex") String sex,
                             @RequestParam("grade") String grade,
                             @RequestParam("number") String number,
                             @RequestParam("major") String major,
                             @RequestParam("location") String location,
                             Map<String,Object> map){
        String s=null;
        Apartment apartment=apartmentMapper.findById(id);
        /*所有学生的默认密码均为123456*/
        String password="123456";
        /*所有学生的默认状态均为在校（未打卡）*/
        String state="在校（未打卡）";
        /*不允许管理员输入其他楼栋的宿舍，后续还应增加检查宿舍是否存在的代码（在宿舍很多的情况下这一步实际上没有太大的作用）*/
        if (location.contains("-")){
            map.put("error","请输入正确格式的宿舍");
            map.put("apa",apartment);
            s="apaAdmin/addStudent";
        }else {
            location=apartment.getApartment()+"-"+location;
            List<Student> students=studentMapper.findByLocation(location);
            if (students.size()>4){
                /*默认每个宿舍最多四个人*/
                map.put("error","当前宿舍已经满员");
                map.put("apa",apartment);
                s="apaAdmin/addStudent";
            }else{
                /*id是主键，当填入相同内容时会报错，使用try catch抛出异常返回前台*/
                try{
                    studentMapper.InsertStudent(stu_id,username,password,sex,grade,number,major,state,location);
                    List<Student> students1=studentMapper.selectByBuildingLike(apartment.getApartment()+"-");
                    map.put("students",students1);
                    map.put("apa",apartment);
                    s="apaAdmin/studentMessage";
                }catch (Exception e){
                    map.put("error","学号已经被使用");
                    map.put("apa",apartment);
                    logger.warn("id为主键，不能重复使用");
                    s="apaAdmin/addStudent";
                }
            }
        }
        return s;
    }
    /*删除学生*/
    @GetMapping("/apaStudentMessage/delete/{id}/{stu_id}")
    public String DeleteStudent(@PathVariable("id") String id,
                                @PathVariable("stu_id") String stu_id,
                                Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        String building=apartment.getApartment()+"-";
        studentMapper.deleteStudent(stu_id);
        List<Student> students=studentMapper.selectByBuildingLike(building);
        map.put("apa",apartment);
        map.put("students",students);
        return "apaAdmin/studentMessage";
    }
    /*进入缺勤页面*/
    @GetMapping("/absences/{id}")
    public String toAbsences(@PathVariable("id") String id,
                             Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        List<Absence> absences=absenceMapper.selectByBuildingLike(apartment.getApartment()+"-");
        map.put("apa",apartment);
        map.put("absences",absences);
        return "apaAdmin/absences";
    }
    /*确认缺勤情况，此处为了防止学生未登录系统触发Controller而造成的缺勤未记录情况*/
    @GetMapping("/absence/confirm/{id}")
    public String confirmAbsence(@PathVariable("id") String id,
                                 Map<String,Object> map) throws ParseException {
        Date dateNow=format1.parse(format1.format(date));
        Date dateBegin=format1.parse(dateBeginS);
        Date dateEnd=format1.parse(dateEndS);
        Apartment apartment=apartmentMapper.findById(id);
        if (dateNow.after(dateBegin)&&dateNow.before(dateEnd)){
            List<Absence> absences=absenceMapper.selectByBuildingLike(apartment.getApartment()+"-");
            map.put("apa",apartment);
            map.put("absences",absences);
            map.put("error","不在确认时间内");
        }else {
            List<Student> students=studentMapper.selectByBuildingLike(apartment.getApartment()+"-");
            /*没有数据的时候进行get和其他的复杂操作会导致报错，直接使用try catch捕获错误并抛出*/
            try{
                for(int i=0;i<students.size();i++){
                    if (students.get(i).getState().equals("在校（未打卡）")){
                        studentMapper.updateState(students.get(i).getStu_id(),"缺勤");
                        absenceMapper.insertAbsence(students.get(i).getStu_id(),students.get(i).getUsername(),dateNow,students.get(i).getLocation(),"未上报");
                    }
                }
                List<Absence> absences=absenceMapper.selectByBuildingLike(apartment.getApartment()+"-");
                map.put("apa",apartment);
                map.put("absences",absences);
            }catch (Exception e){
                List<Absence> absences=absenceMapper.selectByBuildingLike(apartment.getApartment()+"-");
                map.put("apa",apartment);
                map.put("absences",absences);
                logger.info("没有未确认的学生");
            }
        }
        return "apaAdmin/absences";
    }
    /*上报缺勤记录*/
    @GetMapping("/absence/submit/{id}")
    public String submitAbsence(@PathVariable("id") String id,
                                Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        String state="已上报";
        absenceMapper.updateAbsenceState(state,apartment.getApartment()+"-");
        List<Absence> absences=absenceMapper.selectByBuildingLike(apartment.getApartment()+"-");
        map.put("apa",apartment);
        map.put("absences",absences);
        return "apaAdmin/absences";
    }
    /*管理员查询缺勤情况*/
    @PostMapping("/absence/{id}")
    public String selectAbsence(@PathVariable("id") String id,
                                @RequestParam("state") String state,
                                @RequestParam("stu_id") String stu_id,
                                Map<String,Object> map){
        if(state==""){
            state=null;
        }
        if (stu_id==""){
            stu_id=null;
        }
        Apartment apartment=apartmentMapper.findById(id);
        List<Absence> absences=absenceMapper.selectAbsenceLike(apartment.getApartment()+"-",state,stu_id);
        map.put("apa",apartment);
        map.put("absences",absences);
        return "apaAdmin/absences";
    }
}
