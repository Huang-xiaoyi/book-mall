package com.example.springboot.common.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.Account;
import com.example.springboot.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtils {

    private static AdminService staticAdminService;
    private static UserService staticUserService;
    private static MerchantService staticMerchantService;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private MerchantService merchantService;

    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService;
        staticUserService = userService;
        staticMerchantService = merchantService;
    }

    /**
     * 生成token
     */
    public static String genToken(String userRole, String password) {
        return JWT.create().withAudience(userRole) // 将 userId-role 保存到token中
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // token有效时间为2小时
                .sign(Algorithm.HMAC256(password)); // password作为token的密钥
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                log.error("获取当前登录的token失败， token: {}", token);
                return null;
            }
            // 解析token，获取用户的id
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            String role = userRole.split("-")[1];
            Account user = null;
            if ("admin".equals(role)) {
                user = staticAdminService.selectById(Integer.valueOf(userId));
            }
            if ("user".equals(role)) {
                user = staticUserService.selectById(Integer.valueOf(userId));
            }
            if ("merchant".equals(role)) {
                user = staticMerchantService.selectById(Integer.valueOf(userId));
            }

            return user;
        } catch (Exception e) {
            log.error("获取当前登录的管理员信息失败, token={}", token,  e);
            return null;
        }
    }
}