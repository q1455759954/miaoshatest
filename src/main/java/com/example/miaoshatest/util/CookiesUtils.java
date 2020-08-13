package com.example.miaoshatest.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CookiesUtils {

    public static String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        Optional optional = Optional.ofNullable(cookies);
        optional.orElseGet(() -> {
           log.error("cookie为空，请登录！");
           return null;
        });
        if (!optional.isPresent()){
            return null;
        }
        List<String> tokens = Arrays.asList(cookies).stream()
                .filter(str -> str.getName().equals(cookieNameToken))
                .map(cookie -> cookie.getValue())
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(tokens)?"":tokens.get(0);
    }
}
