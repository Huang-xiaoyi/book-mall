package com.example.springboot.service;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.dao.UserDao;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserDao userDao;

    public List<User> selectAll() {
        return userDao.selectAll(null);
    }

    public void updateById(User user) {
        User dbUser = userDao.selectUserByUsername(user.getUsername());
        if (ObjectUtil.isNotEmpty(dbUser)&&dbUser.getId()!=user.getId()){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }


        userDao.updateById(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userDao.selectAll(user);
        return PageInfo.of(userList);
    }

    public void saveUser(User user) {
        User dbUser = userDao.selectUserByUsername(user.getUsername());
        if (ObjectUtil.isNotEmpty(dbUser)){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        else {
            user.setRole("user");
            if (ObjectUtil.isEmpty(user.getAvatar())) {
                user.setAvatar("http://localhost:9090/files/default.jpg");
            }
            userDao.insertUser(user);
        }
    }

    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }

    public User register(User user) {
        //查看该用户名是否已经存在
        User dbUser = userDao.selectUserByUsername(user.getUsername());
        //如果数据库无该用户，可以添加
        if (ObjectUtil.isNotEmpty(dbUser)){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        //存在则抛出异常
        else {
            saveUser(user);
            return user;
        }
    }

    public User login(User user) {
        //查看该用户名是否已经存在
        User dbUser = userDao.selectUserByUsername(user.getUsername());
        //如果数据库无该用户，抛出异常
        if ( dbUser==null){
            throw new ServiceException(ResultCode.USER_NOT_EXIST_ERROR);
        }
         //如果密码不一致 抛出异常
        if (!dbUser.getPassword().equals(user.getPassword())){
            throw new ServiceException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return dbUser;
         }

    public User selectById(Integer id) {
        return userDao.selectById(id);
    }
    public void updatePassword(Account account) {
        User dbUser = userDao.selectUserByUsername(account.getUsername());
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException(ResultCode.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userDao.updateById(dbUser);
    }

}