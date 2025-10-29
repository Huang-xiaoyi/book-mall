package com.example.springboot.dao;

import com.example.springboot.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {

    int insertComment(Comment comment);

    List<Comment> selectCommentByBookId(@Param("BookId") Integer bookId);

    /**
     * 软删除评论（标记为已删除）
     * @param id 评论ID
     * @return 影响的行数
     */
    int softDelete(Integer id);

    /**
     * 硬删除评论（物理删除）
     * @param id 评论ID
     * @return 影响的行数
     */
    int hardDelete(Integer id);
}