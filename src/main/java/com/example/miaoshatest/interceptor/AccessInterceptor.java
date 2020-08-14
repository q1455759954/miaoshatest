package com.example.miaoshatest.interceptor;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.ResultGeekQ;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.redis.RedisClient;
import com.example.miaoshatest.redis.keysbean.AccessKey;
import com.example.miaoshatest.service.IMiaoShaLogic;
import com.example.miaoshatest.util.CookiesUtils;
import com.example.miaoshatest.util.valiadator.UserCheckAndLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import static com.example.miaoshatest.common.CustomerConstant.COOKIE_NAME_TOKEN;

@Service
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    IMiaoShaLogic miaoShaLogic;

    @Autowired
    RedisClient redisClient;

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


            int second = userCheckAndLimit.seconds();
            int maxCount = userCheckAndLimit.maxCount();
            boolean flag = userCheckAndLimit.needLogin();
            if (flag&&user==null){
                return false;
            }
            String key = request.getRequestURL()+"_"+user.getId();

            //*********** redis限流  ***************
            AccessKey accessKey = AccessKey.withExpire(second);
            Integer count = (Integer) redisClient.get(accessKey,key,Integer.class);
            if (count==null){
                redisClient.set(accessKey,key,1);
            }else if (count<maxCount){
                redisClient.incr(accessKey,key);
            }else {
                //超出
                render(response, ResultStatus.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        UserContext.removeUser();
    }

    //**** 回复 ****
    private void render(HttpServletResponse response, ResultStatus cm)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(ResultGeekQ.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
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
