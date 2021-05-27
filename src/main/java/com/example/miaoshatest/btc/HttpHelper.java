package com.example.miaoshatest.btc;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *  http请求类
 * @author EDZ
 */
@Slf4j
public class HttpHelper {

    /**
     *  发送http get请求
     */
    public static String sendGetRequest(String url) throws Exception{
        String result = HttpRequest.get(url)
                .setHttpProxy("localhost",10809)
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .execute()
                .body();
        return result;
    }

    /**
     *  发送http post请求
     */
    public static String sendPostRequest(String url ,String json) {
        String result = HttpRequest.post(url)
                .setHttpProxy("localhost",10809)
                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(json)
                .execute().body();
        return result;
    }


}
