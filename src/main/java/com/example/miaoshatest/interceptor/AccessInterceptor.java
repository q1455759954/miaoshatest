package com.example.miaoshatest.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.service.IMiaoShaLogic;
import com.example.miaoshatest.util.CookiesUtils;
import com.example.miaoshatest.util.valiadator.UserCheckAndLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.miaoshatest.common.CustomerConstant.COOKIE_NAME_TOKEN;

@Service
@Slf4j
public class AccessInterceptor implements HandlerInterceptor {


    @Autowired
    IMiaoShaLogic miaoShaLogic;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof ResourceHttpRequestHandler) {
            log.info("---------ResourceHttpRequestHandler-------" + handler.toString() + "------------");
        }else if(handler instanceof HandlerMethod) {
            log.info("打印拦截方法handler ：{} ",handler);
            HandlerMethod hm  = (HandlerMethod)handler;
            MiaoShaUser user = getUser(request,response);
            UserContext.setUser(user);
            UserCheckAndLimit userCheckAndLimit = hm.getMethodAnnotation(UserCheckAndLimit.class);
            if (userCheckAndLimit==null){
                return true;
            }

        }
        return false;
    }

    private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter(COOKIE_NAME_TOKEN);
        String cookie = CookiesUtils.getCookieValue(request, COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(token) && StringUtils.isEmpty(cookie)){
            return null;
        }
        String t = StringUtils.isEmpty(token)?cookie:token;
        return miaoShaLogic.getUserByToken(response,t);
    }
}
