package com.example.springboot.common.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboot.common.ResultCode;
import com.example.springboot.entity.Account;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.MerchantService;
import com.example.springboot.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;

    @Resource
    private MerchantService merchantService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从http请求的header中获取token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            // 获取参数
            token = request.getParameter("token");
        }
        // 2. 开始执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(ResultCode.TOKEN_INVALID_ERROR);
        }
        Account user = null;
        try {
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];
            // 根据token中的userid获取用户信息
            if ("admin".equals(role)) {
                user = adminService.selectById(Integer.valueOf(userId));
            }
            if ("user".equals(role)) {
                user = userService.selectById(Integer.valueOf(userId));
            }
            if ("merchant".equals(role)) {
                user = merchantService.selectById(Integer.valueOf(userId));
            }


        } catch (Exception e) {
            throw new ServiceException(ResultCode.TOKEN_CHECK_ERROR);
        }
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        try {
            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            // 验证token
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(ResultCode.TOKEN_CHECK_ERROR);
        }
        return true;
    }
}