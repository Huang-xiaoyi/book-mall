package com.example.springboot.dao;

import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<User> selectAll(User user);
void insertUser(User user);
void deleteById(Integer id);
    void updateById(User user);
    User selectUserByUsername(String username);
    User selectById(Integer id);
}
