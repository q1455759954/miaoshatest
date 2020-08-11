package com.example.miaoshatest.interceptor;

import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.util.CookiesUtils;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof ResourceHttpRequestHandler) {
            log.info("---------ResourceHttpRequestHandler-------" + handler.toString() + "------------");
        }else if(handler instanceof HandlerMethod) {
            log.info("打印拦截方法handler ：{} ",handler);
            HandlerMethod hm  = (HandlerMethod)handler;
            MiaoShaUser user = getUser(request,response);
        }
        return false;
    }

    private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter(COOKIE_NAME_TOKEN);
        String cookie = CookiesUtils.getCookieValue(request, COOKIE_NAME_TOKEN);
        return null;
    }
}
