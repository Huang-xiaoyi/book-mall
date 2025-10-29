package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Book;
import com.example.springboot.entity.Comment;
import com.example.springboot.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
//类中所有方法添加@ResopnseBody，这一注解会向前端返回Json/XML类型的数据并写入HTTP响应体
@RequestMapping("/comment")
public class CommentController {
    @Resource
    CommentService commentService;

    @GetMapping("/select")
    public Result selectCommentByBookId(Integer BookId){
        List<Comment> comments = commentService.selectCommentByBookId(BookId);
        return Result.success(comments);
    }
}
