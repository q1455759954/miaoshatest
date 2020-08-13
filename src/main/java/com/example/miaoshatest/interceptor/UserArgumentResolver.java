package com.example.miaoshatest.interceptor;

import com.example.miaoshatest.dao.bean.MiaoShaUser;
import com.example.miaoshatest.entity.ResultStatus;
import com.example.miaoshatest.exception.UserException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoShaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //在AccessInterceptor类中保存了User到ThreadLocal，能获取同一线程下的user
        MiaoShaUser user = Optional.ofNullable(UserContext.getUser()).orElseThrow(()->new UserException(ResultStatus.USER_NOT_EXIST));
        return  user;
    }
}
