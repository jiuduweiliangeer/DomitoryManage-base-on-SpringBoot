package com.example.college.mapper;

import com.example.college.pojo.Apartment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentMapper {
    public Apartment findById(String id);
    public void setNewPassword(String id, String password);
    public void setExcludeStateAndEmail(String id, String username, String password, String sex, String age, String phone, String apartment, String apa_sex);
    public List<Apartment> selectBySchoolAdmin(String sex, String apartment);
    public List<Apartment> findAll();
    public void deleteApartment(String id);
    public void insertApartment(String id, String username, String password, String sex, String age, String phone, String apartment, String apa_sex);
    public Apartment findEmail(String email);
    public void setEmail(String id, String email);
    public List<Apartment> findByDistribution(String apartment, String apa_sex, String username);
}
