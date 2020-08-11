package com.example.miaoshatest.service;

import com.example.miaoshatest.entity.LoginVo;
import com.example.miaoshatest.entity.ResultGeekQ;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    public ResultGeekQ<String> login(HttpServletResponse response, LoginVo loginVo);


}
