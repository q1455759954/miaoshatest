package com.example.miaoshatest.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.MiaoShaUserKey;
import com.example.miaoshatest.redis.keysbean.MiaoshaKey;
import com.example.miaoshatest.service.IMiaoShaLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.example.miaoshatest.common.CustomerConstant.COOKIE_NAME_TOKEN;

@Service
@Slf4j
public class MiaoshaLogicImpl implements IMiaoShaLogic {

    @Autowired
    private RedisClient redisClient;

    @Override
    public HttpServletResponse addCookie(HttpServletResponse response, String token, MiaoShaUser user) {
        redisClient.set(MiaoShaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置有效期
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
        return response ;
    }

    @Override
    public MiaoShaUser getUserByToken(HttpServletResponse response, String t) {
        if (StringUtils.isEmpty(t)){
            return null;
        }
        MiaoShaUser user = (MiaoShaUser) redisClient.get(MiaoShaUserKey.token,t,MiaoShaUser.class);
        if (user!=null){
            addCookie(response,t,user);
        }
        return user;
    }


}
