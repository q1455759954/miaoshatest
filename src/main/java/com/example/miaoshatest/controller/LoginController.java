package com.example.miaoshatest.controller;

import com.alibaba.fastjson.JSON;
import com.example.miaoshatest.entity.AbstractResult;
import com.example.miaoshatest.entity.LoginVo;
import com.example.miaoshatest.entity.ResultGeekQ;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    IUserService userService;

    @RequestMapping("/to_login")
    public String tologin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public ResultGeekQ<String> tologin(@Valid LoginVo loginVo, HttpServletResponse response) {
        log.info("登录开始 start! loginvo:{}", JSON.toJSON(loginVo));

        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        try {
            ResultGeekQ result = userService.login(response,loginVo);
            if(!AbstractResult.isSuccess(result)){
                resultGeekQ.withError(result.getCode(),result.getMessage());
                return resultGeekQ;
            }
        } catch (Exception e) {
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
        }
        return resultGeekQ;
    }


}
