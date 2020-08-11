package com.example.miaoshatest.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;


/**
 * @auther 邱润泽 bullock
 * @date 2019/11/10
 */
@Service
@Slf4j
public class RedisClient<T> {

    @Autowired
    private JedisCluster jedis;

    public void set(String key, String value) throws Exception {
        try {
            jedis.set(key, value);
        } finally {
            //返还到连接池
        }
    }


    /**
     * 向缓存中设置对象
     *
     * @param key
     * @param value
     * @return
     */
    public  boolean set(String key, Object value) {
        try {
            String objectJson = JSON.toJSONString(value);
            if (jedis != null) {
                jedis.set(key, objectJson);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(RedisKeyPrefix prefix, String key, T value) {
        try {
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(RedisKeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
        }
    }


    public boolean set(String key, String value, String nxxx, String expx, long time) {
        try {
            String result = "";
            if(jedis != null){
                result = jedis.set(key, value, nxxx, expx, time);

            }
            if ("OK".equals(result)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Redis缓存设置key值 出错！", e);
            return false;
        } finally {
        }
    }


    /**
     * 获取当个对象
     * */
    public <T> T get(RedisKeyPrefix prefix, String key,  Class<T> clazz) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = jedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
        }
    }


    /**
     * 获取当个对象
     * */
    public <T> T get(String key,  Class<T> clazz) {
        try {
            //生成真正的key
            String realKey  =  key;
            String  str = jedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
        }
    }

    /**
     * <p>
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * </p>
     *
     * @param key
     * @return 加值后的结果
     */
    public Long incr(String key) {
        Long res = null;
        try {
            res = jedis.incr(key);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
        }
        return res;
    }

    /**
     * 增加值
     * */
    public <T> Long incr(RedisKeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
        }
    }

    /**
     * 删除
     * */
    public boolean delete(RedisKeyPrefix prefix, String key) {
        try {
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(realKey);
            return ret > 0;
        }finally {
        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }


    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
