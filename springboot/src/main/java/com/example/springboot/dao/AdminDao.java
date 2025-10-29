package com.example.springboot.dao;

import com.example.springboot.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {
    List<Admin> selectAll(Admin admin);
void insertAdmin(Admin admin);
void deleteById(Integer id);
    void updateById(Admin admin);
    Admin selectAdminByUsername(String username);
    Admin selectById(Integer id);
}
