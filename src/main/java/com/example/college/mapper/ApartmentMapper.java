package com.example.college.mapper;

import com.example.college.pojo.Apartment;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentMapper {
    public Apartment findById(String id);
}
