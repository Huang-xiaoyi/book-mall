package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.dao.TypeDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Type;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeService {
    @Resource
    TypeDao typeDao;

    public List<Type> selectAll() {
        return typeDao.selectAll(null);
    }

    public void updateById(Type type) {
        typeDao.updateById(type);
    }

    public PageInfo<Type> selectPage(Type type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Type> typeList = typeDao.selectAll(type);
        return PageInfo.of(typeList);
    }

    public void saveType(Type type) {
        typeDao.insertType(type);
    }

    public void deleteById(Integer id) {
        typeDao.deleteById(id);
    }

}