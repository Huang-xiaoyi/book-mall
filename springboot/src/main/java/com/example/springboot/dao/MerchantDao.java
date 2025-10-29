package com.example.springboot.dao;

import com.example.springboot.entity.Merchant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantDao {
    List<Merchant> selectAll(Merchant merchant);
    void insertMerchant(Merchant merchant);
void deleteById(Integer id);
    void updateById(Merchant merchant);
    Merchant selectMerchantByUsername(String username);

    Merchant selectById(Integer id);
}
