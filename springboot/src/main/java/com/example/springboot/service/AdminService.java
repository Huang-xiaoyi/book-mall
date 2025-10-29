package com.example.springboot.service;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.dao.AdminDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminService {
    @Resource
    AdminDao adminDao;

    public List<Admin> selectAll() {
        return adminDao.selectAll(null);
    }

    public void updateById(Admin admin) {
        Admin dbAdmin = adminDao.selectAdminByUsername(admin.getUsername());
        if (ObjectUtil.isNotEmpty(dbAdmin)&&dbAdmin.getId()!=admin.getId()){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        adminDao.updateById(admin);
    }
    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> adminList = adminDao.selectAll(admin);
        return PageInfo.of(adminList);
    }
    public void saveAdmin(Admin admin) {
        if (ObjectUtil.isEmpty(admin.getAvatar())) {
            admin.setAvatar("http://localhost:9090/files/default.jpg");
        }
        admin.setRole("admin");
        adminDao.insertAdmin(admin);
    }

    public void deleteById(Integer id) {
        adminDao.deleteById(id);
    }

    public Admin login(Admin admin) {
        //查看该用户名是否已经存在
        Admin dbAdmin = adminDao.selectAdminByUsername(admin.getUsername());
        //如果数据库无该用户，抛出异常
        if ( ObjectUtil.isEmpty(dbAdmin)){
            throw new ServiceException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        //如果密码不一致 抛出异常
        if (!dbAdmin.getPassword().equals(admin.getPassword())){
            throw new ServiceException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return dbAdmin;
    }
    public Admin selectById(Integer id) {
        return adminDao.selectById(id);
    }
    public void updatePassword(Account account) {
        Admin dbUser = adminDao.selectAdminByUsername(account.getUsername());
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException(ResultCode.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        adminDao.updateById(dbUser);
    }
}