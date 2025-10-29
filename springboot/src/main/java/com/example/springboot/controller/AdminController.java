package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Admin> list = adminService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Admin admin,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
      PageInfo<Admin> pageInfo=   adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin){
        adminService.saveAdmin(admin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/update")
    public Result updateById(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return Result.success();
    }

}
