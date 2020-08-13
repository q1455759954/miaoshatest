package com.example.miaoshatest.service;

import com.example.miaoshatest.dao.bean.MiaoShaUser;

import javax.servlet.http.HttpServletResponse;

public interface IMiaoShaLogic {

    public HttpServletResponse addCookie(HttpServletResponse response, String token, MiaoShaUser user);

    MiaoShaUser getUserByToken(HttpServletResponse response, String t);
}
