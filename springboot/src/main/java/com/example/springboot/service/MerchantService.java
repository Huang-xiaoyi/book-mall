package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.dao.MerchantDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Merchant;
import com.example.springboot.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MerchantService {
    @Resource
    MerchantDao merchantDao;

    public List<Merchant> selectAll() {
        return merchantDao.selectAll(null);
    }

    public void updateById(Merchant merchant) {
        Merchant dbMerchant = merchantDao.selectMerchantByUsername(merchant.getUsername());
        if (ObjectUtil.isNotEmpty(dbMerchant)&&dbMerchant.getId()!=merchant.getId()){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        merchantDao.updateById(merchant);
    }
    public PageInfo<Merchant> selectPage(Merchant merchant, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Merchant> merchantList = merchantDao.selectAll(merchant);
        return PageInfo.of(merchantList);
    }
    public void saveMerchant(Merchant merchant) {
        if (ObjectUtil.isEmpty(merchant.getAvatar())) {
            merchant.setAvatar("http://localhost:9090/files/default.jpg");
        }
        merchant.setRole("merchant");
        merchantDao.insertMerchant(merchant);
    }

    public void deleteById(Integer id) {
        merchantDao.deleteById(id);
    }

    public Merchant login(Merchant merchant) {
        //查看该用户名是否已经存在
        Merchant dbMerchant = merchantDao.selectMerchantByUsername(merchant.getUsername());
        //如果数据库无该用户，抛出异常
        if ( ObjectUtil.isEmpty(dbMerchant)){
            throw new ServiceException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        //如果密码不一致 抛出异常
        if (!dbMerchant.getPassword().equals(merchant.getPassword())){
            throw new ServiceException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return dbMerchant;
    }
    public Merchant selectById(Integer id) {
        return merchantDao.selectById(id);
    }
    public void updatePassword(Account account) {
        Merchant dbUser = merchantDao.selectMerchantByUsername(account.getUsername());
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException(ResultCode.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        merchantDao.updateById(dbUser);
    }
}