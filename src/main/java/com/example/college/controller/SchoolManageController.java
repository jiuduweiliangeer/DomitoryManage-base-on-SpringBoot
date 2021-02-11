package com.example.college.controller;

import com.example.college.mapper.*;
import com.example.college.pojo.Apartment;
import com.example.college.pojo.Location;
import com.example.college.pojo.School;
import com.example.college.pojo.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Controller
public class SchoolManageController {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    AbsenceMapper absenceMapper;
    @Autowired
    HistoryMapper historyMapper;
    @Autowired
    LocationMapper locationMapper;
    @Autowired
    ApartmentMapper apartmentMapper;
    String identity="学校管理员";
    Logger logger= LoggerFactory.getLogger(getClass());
    /*前往主界面，管理员信息*/
    @GetMapping("/schoolMain/{id}")
    public String toSchoolMain(@PathVariable("id") String id,
                               Map<String,Object> map){
        School school=schoolMapper.findById(id);
        List<Apartment> apartments=apartmentMapper.findAll();
        map.put("sch",school);
        map.put("apartments",apartments);
        return "schAdmin/schoolMain";
    }
    /*查询管理员相关信息*/
    @PostMapping("/schoolMain/{id}")
    public String selectAdmin(@PathVariable("id") String id,
                              @RequestParam("sex") String sex,
                              @RequestParam("apartment") String apartment,
                              Map<String,Object> map){
        if (sex == "") {
            sex=null;
        }
        if (apartment==""){
            apartment=null;
        }
        School school=schoolMapper.findById(id);
        List<Apartment> apartments=apartmentMapper.selectBySchoolAdmin(sex,apartment);
        map.put("sch",school);
        map.put("apartments",apartments);
        return "schAdmin/schoolMain";
    }
    /*编辑管理员*/
    @GetMapping("/schoolMain/editAdministrator/{id}/{sch_id}")
    public String toEditAdministrator(@PathVariable("id") String id,
                                      @PathVariable("sch_id") String sch_id,
                                      Map<String,Object> map){
        School school=schoolMapper.findById(sch_id);
        Apartment apartment=apartmentMapper.findById(id);
        map.put("sch",school);
        map.put("apa",apartment);
        return "schAdmin/editAdministrator";
    }
    /*修改管理员相关信息*/
    @PostMapping("/schoolMain/editAdministrator/{id}/{sch_id}")
    public String editAdministrator(@PathVariable("id") String id,
                                    @PathVariable("sch_id") String sch_id,
                                    @RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("sex") String sex,
                                    @RequestParam("age") String age,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("apartment") String apartment,
                                    Map<String,Object> map){
        String s=null;
        School school=schoolMapper.findById(sch_id);
        Apartment apartment1=apartmentMapper.findById(id);
        if (apartment.contains("-")){
            map.put("sch",school);
            map.put("apa",apartment1);
            map.put("error","请输入正确的楼栋号");
            s="schAdmin/editAdministrator";
        }else {
            apartmentMapper.setExcludeStateAndEmail(id,username,password,sex,age,phone,apartment);
            List<Apartment> apartments=apartmentMapper.findAll();
            map.put("sch",school);
            map.put("apartments",apartments);
            s="schAdmin/schoolMain";
        }
        return s;
    }
    /*进入管理员楼栋详情页面*/
    @GetMapping("/schoolMain/describe/{id}/{sch_id}")
    public String toDescribeApa(@PathVariable("id") String id,
                             @PathVariable("sch_id") String sch_id,
                             Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(sch_id);
        List<Student> students=studentMapper.selectByBuildingLike(apartment.getApartment()+"-");
        /*两种判定方法，现在使用的是优化过的方法，
         * 未优化过的方法中，只能将有缺勤的寝室改成有缺勤状态，而不能改回，并且反复调用sql语句会造成冗余
         *                逻辑为：
         *                   循环以管理员管理楼栋字段为关键字段的模糊查询以后获得的集合，
         *                   对集合中每一个数据的state进行判断，当state是缺勤状态时，将
         *                   宿舍的状态修改为有缺勤
         * 优化过的方法中，可以将有缺勤的寝室改成有缺勤状态，也可以将缺勤状态改回，将sql语句抽出，整个逻辑中实际只在需要使用
         * sql改变的时候调用
         *                逻辑为：
         *                   循环以管理员管理楼栋字段为关键字段的模糊查询以后获得的集合，
         *                   对集合中每一个数据的state进行判断，当state是在校（未打卡）
         *                   状态时，获取当前学生的宿舍信息，并在学生表中查询宿舍中的其他
         *                   学生信息，整合为一个集合，遍历这个集合，当集合中有缺勤状态的
         *                   数据时，将宿舍的状态修改为有缺勤，当所有的数据的状态都为在校
         *                   （未打卡）或者已打卡状态，则将宿舍状态修改为在寝*/
        try {
            int in=0;
            int out=0;
            for (int i=0;i<students.size();i++){
                String is_home="在寝";//每次进入循环的时候都需要设定is_home的默认值
                /*if (students.get(i).getState().equals("缺勤")){
                    String[] split=students.get(i).getLocation().split("-");*//*将学生的楼栋和宿舍分离，方便后续查询*//*
                    String floor=split[1];*//*得到宿舍号*//*
                    String is_home="有缺勤";
                    *//*进行修改，floor为分割后的学生宿舍号，用宿舍管理员的楼栋号和学生的宿舍号来进行修改和查询*//*
                    locationMapper.UpdateIs_home(apartment.getApartment(),floor,is_home);
                }*/
                if (students.get(i).getState().equals("在校（未打卡）")||students.get(i).getState().equals("已打卡")){
                    String[] split=students.get(i).getLocation().split("-");
                    String floor=split[1];
                    List<Student> studentsLocation=studentMapper.findByLocation(students.get(i).getLocation());
                    for (int j=0;j<studentsLocation.size();j++){
                        if (studentsLocation.get(j).getState().equals("缺勤")){
                            is_home="有缺勤";
                        }/*else if (is_home.equals("在寝")){
                         *//*为什么要添加一个判断？
                         * 如果得到的集合在校（未打卡）状态在后面，会直接进入else，
                         * 如果没有这个判断，就会将is_home给设定为在寝状态，这样会
                         * 导致无法正确的更改宿舍状态为有缺勤，
                         * 因此，设置一个判定，只有当他本身就是在寝状态时，才可以进入这个判定
                         * ps:is_home的默认值就是在寝，如果集合中的全部数据都不是缺勤状态，则
                         * is_home不会变更，所以是否有必要加这个判定？当is_home的默认值直接设定为
                         * 在寝状态，没有必要设定这个判定*//*
                            is_home="在寝";
                        }*/
                    }
                    locationMapper.UpdateIs_home(apartment.getApartment(),floor,is_home);
                }
            }
        }catch (Exception e){
            logger.info("本楼栋没有数据");
        }
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("sch",school);
        map.put("locations",locations);
        return "schAdmin/describeLocation";
    }
    /*查询宿舍相关*/
    @PostMapping("/schoolMain/describe/{id}/{sch_id}")
    public String SelectLocationSch(@PathVariable("id") String id,
                                 @PathVariable("sch_id") String sch_id,
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
        School school=schoolMapper.findById(sch_id);
        map.put("apa",apartment);
        map.put("sch",school);
        map.put("locations",locations);
        return "schAdmin/describeLocation";
    }
    /*标记宿舍，修改宿舍状态为危险*/
    @GetMapping("/schoolMain/describe/sign/{id}/{floor}/{sch_id}")
    public String signLocationSch(@PathVariable("id") String id,
                               @PathVariable("floor") String floor,
                               @PathVariable("sch_id") String sch_id,
                               Map<String,Object> map) throws ParseException {
        String state="危险";
        String operate="标记宿舍";
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(sch_id);
        locationMapper.UpdateState(apartment.getApartment(),floor,state);
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("sch",school);
        map.put("locations",locations);
        return "schAdmin/describeLocation";
    }
    /*取消标记，修改宿舍状态为安全，URL的变化通过前台判定宿舍的状态来进行修改，具体操作见前台*/
    @GetMapping("/schoolMain/describe/resign/{id}/{floor}/{sch_id}")
    public String resignLoctaionSch(@PathVariable("id") String id,
                                 @PathVariable("floor") String floor,
                                 @PathVariable("sch_id") String sch_id,
                                 Map<String,Object> map) throws ParseException {
        String state="安全";
        String operate="取消标记";
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(sch_id);
        locationMapper.UpdateState(apartment.getApartment(),floor,state);
        List<Location> locations=locationMapper.findByBuilding(apartment.getApartment());
        map.put("apa",apartment);
        map.put("sch",school);
        map.put("locations",locations);
        return "schAdmin/describeLocation";
    }
    /*查看某个宿舍的详细信息*/
    @GetMapping("/schoolMain/describe/examine/{id}/{floor}/{sch_id}")
    public String examineLoctaionApa(@PathVariable("id") String id,
                                  @PathVariable("floor") String floor,
                                  @PathVariable("sch_id") String sch_id,
                                  Map<String,Object> map){
        Apartment apartment=apartmentMapper.findById(id);
        School school=schoolMapper.findById(sch_id);
        String location=apartment.getApartment()+"-"+floor;
        List<Student> students=studentMapper.findByLocation(location);
        map.put("apa",apartment);
        map.put("sch",school);
        map.put("students",students);
        return "schAdmin/locationMessage";
    }
    /*删除宿舍管理员*/
    @GetMapping("/schoolMain/delete/{id}/{sch_id}")
    public String deleteApartment(@PathVariable("id") String id,
                                  @PathVariable("sch_id") String sch_id,
                                  Map<String,Object> map){
        apartmentMapper.deleteApartment(id);
        School school=schoolMapper.findById(sch_id);
        List<Apartment> apartments=apartmentMapper.findAll();
        map.put("sch",school);
        map.put("apartments",apartments);
        return "schAdmin/schoolMain";
    }
    /*进入添加宿管页面*/
    @GetMapping("/addAdministrator/{id}")
    public String toAddAdministrator(@PathVariable("id") String id,
                                     Map<String,Object> map){
        School school=schoolMapper.findById(id);
        map.put("sch",school);
        return "schAdmin/addAdministrator";
    }
    /*添加宿管，
    * 2021/2/11记录：
    * 添加用户时，不能只判断主键，还需要判断另外身份用户的id是否重复，如果重复会导致登录混淆*/
    @PostMapping("/addAdministrator/{id}")
    public String addAdministrator(@PathVariable("id") String id,
                                   @RequestParam("id") String apa_id,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("sex") String sex,
                                   @RequestParam("age") String age,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("apartment") String apartment,
                                   Map<String,Object> map){
        String s=null;
        boolean t=true;
        School school=schoolMapper.findById(id);
        List<Apartment> apartments=apartmentMapper.findAll();
        try {
            List<Student> students=studentMapper.findAll();
            for (int i=0;i<students.size();i++){
                if (apa_id.equals(students.get(i).getStu_id())){
                    t=false;
                }
            }
            if (t){
                if (!apartment.contains("-")){
                    apartmentMapper.insertApartment(apa_id,username,password,sex,age,phone,apartment);
                    List<Apartment> apartments1=apartmentMapper.findAll();
                    map.put("sch",school);
                    map.put("apartments",apartments1);
                    s="schAdmin/schoolMain";
                }else {
                    map.put("sch",school);
                    map.put("apartments",apartments);
                    map.put("error","请输入正确的楼栋号");
                    s="schAdmin/addAdministrator";
                }
            }else {
                map.put("sch",school);
                map.put("apartments",apartments);
                map.put("error","请输入正确的id");
                s="schAdmin/addAdministrator";
            }
        }catch (Exception e){
            map.put("sch",school);
            map.put("apartments",apartments);
            map.put("error","请输入正确的id");
            s="schAdmin/addAdministrator";
        }
        return s;
    }
}
