package com.example.springboot.dao;

import com.example.springboot.entity.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDao {
    List<Type> selectAll(Type type);
void insertType(Type type);
void deleteById(Integer id);
    void updateById(Type type);
}
