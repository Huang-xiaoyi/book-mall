package com.example.springboot.service;

import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.CartDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Cart;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartService {
    @Resource
    private CartDao cartDao;

    public PageInfo<Cart> SelectPage(Integer PageNum, Integer PageSize){
        Integer userid = JwtTokenUtils.getCurrentUser().getId();
        PageHelper.startPage(PageNum, PageSize);
        List<Cart> carts = cartDao.SelectUserCart(userid);
        return PageInfo.of(carts);
    }

    public void AddCart(Cart cart){

    }

}
