package com.example.springboot.service;

import com.example.springboot.dao.CommentDao;
import com.example.springboot.entity.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Resource
    CommentDao commentDao;

    public void insertComment(Comment comment){
        Date date = new Date();
        comment.setCreateTime(date);
        comment.setIsDeleted((short) 0);
        commentDao.insertComment(comment);
    }

    public List<Comment> selectCommentByBookId(Integer bookId){
        return commentDao.selectCommentByBookId(bookId);
    }

    public int softDelete(Integer commentId){
        return commentDao.softDelete(commentId);
    }

    public int hardDelete(Integer commentId){
        return commentDao.hardDelete(commentId);
    }
}
