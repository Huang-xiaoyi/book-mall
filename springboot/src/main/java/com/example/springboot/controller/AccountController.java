package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Merchant;
import com.example.springboot.entity.User;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.MerchantService;
import com.example.springboot.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    AdminService adminService;

    @Resource
    UserService userService;

    @Resource
    MerchantService merchantService;


    /**
     * 注册接口
     */

    @PostMapping("/register")
    public Result accountRegister(@RequestBody Account account){
    /*
    //注册为多个角色
     String role = account.getRole();
        Account register = new Account(); // 定义一个Account类的register,用于返回给前端
        if (role.equals("user")){ //这里只有用户注册 如果有其他角色也需要注册，也是同样的逻辑
            User user = new User();
            BeanUtils.copyProperties(account,user);
            register = userService.register(user);
        }*/
//注册为一个角色
        Account register = new Account();
        User user = new User();
        //把后者的属性复制一份道前者
        BeanUtils.copyProperties(account,user);
        register = userService.register(user);

        return Result.success(register);
    }


    /**
     * 用户登录接口
     */

    @PostMapping("/login")
    public Result AccountLogin(@RequestBody Account account){
        String role = account.getRole();
        Account login = new Account(); // 定义一个Account类的login,用于返回给前端
        if (role.equals("admin")){
            Admin admin = new Admin(); //创建一个管理员类admin
            BeanUtils.copyProperties(account,admin); //把父类的属性拷贝到admin
            login = adminService.login(admin); //调用服务层的登录逻辑
        }
        if (role.equals("user")){
            User user = new User();
            BeanUtils.copyProperties(account,user);
            login = userService.login(user);
        }
        if (role.equals("merchant")){
            Merchant merchant = new Merchant();
            //BeanUtils.copyProperties(A, B)把A的属性赋值到B中
            BeanUtils.copyProperties(account,merchant);
            login = merchantService.login(merchant);
        }

        //        生成token
        String token = JwtTokenUtils.genToken(login.getId() + "-" + login.getRole(), login.getPassword());
        //        创建一个键值对map集合，把token和user塞进去，返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", login);
        return Result.success(map); //装好数据后，把map结合返回给前端

    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {

        if (account.getRole().equals("admin")) {
            adminService.updatePassword(account);
        }
        if (account.getRole().equals("user")) {
            userService.updatePassword(account);
        }
        if (account.getRole().equals("merchant")) {
            merchantService.updatePassword(account);
        }

        return Result.success();
    }

}
