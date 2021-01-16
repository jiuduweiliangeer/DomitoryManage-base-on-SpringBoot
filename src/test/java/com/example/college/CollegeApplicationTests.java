package com.example.college;

import com.example.college.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class CollegeApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    StudentMapper studentMapper;
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

}
