package com.example.springboot.dao;

import com.example.springboot.entity.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeDao {
    List<Notice> selectAll(Notice notice);
void insertNotice(Notice notice);
void deleteById(Integer id);
    void updateById(Notice notice);
}
