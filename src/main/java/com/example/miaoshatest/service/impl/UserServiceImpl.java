package com.example.miaoshatest.service.impl;

import com.example.miaoshatest.dao.IUserDao;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.LoginVo;
import com.example.miaoshatest.entity.ResultGeekQ;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.service.IMiaoShaLogic;
import com.example.miaoshatest.service.IUserService;
import com.example.miaoshatest.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDao userDao;
    @Autowired
    IMiaoShaLogic mSLogic;

    @Override
    public ResultGeekQ<String> login(HttpServletResponse response, LoginVo loginVo) {
        ResultGeekQ resultGeekQ = ResultGeekQ.build();

        try{
            String mobile = loginVo.getMobile();
            String password = loginVo.getPassword();
            MiaoShaUser user = userDao.getUserByMobile(mobile);
            if (user==null){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.MOBILE_NOT_EXIST);
                return resultGeekQ;
            }
            String md5Pass = MD5Util.formPassToDBPass(password,user.getSalt());
            if (!md5Pass.equals(user.getPassword())){
                resultGeekQ.withErrorCodeAndMessage(ResultStatus.PASSWORD_ERROR);
                return resultGeekQ;
            }
            String token  = UUID.randomUUID().toString().replace("-","");
            mSLogic.addCookie(response, token, user);
        }catch (Exception e){
            log.error("登陆发生错误 error:{}",e);
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
            return resultGeekQ;
        }
        return resultGeekQ;
    }

}
