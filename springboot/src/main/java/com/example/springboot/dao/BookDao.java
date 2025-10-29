package com.example.springboot.dao;

import com.example.springboot.entity.Book;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao {
    List<Book> selectAll(Book book);
void insertBook(Book book);
void deleteById(Integer id);
    void updateById(Book book);
    Book selectById(Integer id);
    List<Book> selectBookByName(Book book);
    List<Book> selectByTypeId(@Param("typeId")Integer typeId);
//    @Param使得映射文件能够将这一参数与SQL中的参数相关联
}
