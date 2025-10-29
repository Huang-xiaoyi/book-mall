package com.example.springboot.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<User> list = userService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(User user,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
      PageInfo<User> pageInfo=   userService.selectPage(user, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/save")
    public Result save(@RequestBody User user){

        userService.saveUser(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/update")
    public Result updateById(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

}
