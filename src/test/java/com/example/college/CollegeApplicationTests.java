package com.example.college;

import com.example.college.mapper.ClockMapper;
import com.example.college.mapper.StudentMapper;
import com.example.college.pojo.Clock;
import com.example.college.pojo.Student;
import org.apache.ibatis.javassist.Loader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class CollegeApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    ClockMapper clockMapper;
    @Test
    public void modify(){
        studentMapper.setNewPassword("1740611330","wangjiu");
        System.out.println(studentMapper.findById("1740611330").toString());
    }
    @Test
    void contextLoads() {
        System.out.println(dataSource.getClass());
        try {
            Connection connection=dataSource.getConnection();
            System.out.println(connection);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    Date date=new Date();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    @Test
    void testTime() throws ParseException {
        String date1=format.format(date);
        Date inserDate=format.parse(date1);
        clockMapper.insertAll("1740611330",inserDate);
        Clock clock=clockMapper.find("1740611330");
        System.out.println(clock.toString());

    }
/*    @Test
    public void testSelectRelative(){
        List<Student> students=studentMapper.selectByRelative(null,"wangjiu",null);
        System.out.println(students);
    }*/
}
