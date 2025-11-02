package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Cart;
import com.example.springboot.service.CartService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;

    @GetMapping("/carts")
    public Result SelectUserCart(
                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Cart> pageInfo = cartService.SelectPage(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/insert")
    public Result AddCart(Cart cart){
        return null;
    }
}
