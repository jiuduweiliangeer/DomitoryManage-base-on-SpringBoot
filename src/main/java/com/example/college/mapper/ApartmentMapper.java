package com.example.college.mapper;

import com.example.college.pojo.Apartment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentMapper {
    public Apartment findById(String id);
    public void setNewPassword(String id,String password);
    public void setExcludeStateAndEmail(String id,String username,String password,String sex,String age,String phone,String apartment);
    public List<Apartment> selectBySchoolAdmin(String sex,String apartment);
    public List<Apartment> findAll();
    public void deleteApartment(String id);
    public void insertApartment(String id,String username,String password,String sex,String age,String phone,String apartment);
    public Apartment findEmail(String email);
    public void setEmail(String id,String email);
}
