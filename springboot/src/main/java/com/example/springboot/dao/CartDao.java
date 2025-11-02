package com.example.springboot.dao;

import com.example.springboot.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao {
    public List<Cart> SelectUserCart(Integer userid);
    public void AddCart(Cart cart);
}
