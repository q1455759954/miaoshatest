package com.example.miaoshatest.redis;

public interface RedisKeyPrefix {

    public int expireSeconds() ;

    public String getPrefix() ;

}
