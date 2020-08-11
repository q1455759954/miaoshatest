package com.example.miaoshatest.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Setter
@Getter
@Slf4j
public class RedisConfig {
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.timeout}")
//    private int timeout;//秒
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int poolMaxIdle;
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private int poolMaxWait;//秒
//
//    @Bean
//    public JedisPool redisPoolFactory()  throws Exception{
//        log.info("JedisPool注入成功！！");
//        log.info("redis地址：" + host + ":" + port);
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(poolMaxIdle);
//        jedisPoolConfig.setMaxWaitMillis(1000);
//        // 是否启用pool的jmx管理功能, 默认true
//        jedisPoolConfig.setJmxEnabled(true);
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
//        return jedisPool;
//    }

    @Bean
    public JedisCluster redisClusterFactory()  throws Exception{
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);
        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6380));
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6381));
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6382));
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6383));
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6384));
        jedisClusterNode.add(new HostAndPort("188.131.218.191", 6385));
        JedisCluster jedisCluster = null;
        try {
            //connectionTimeout：指的是连接一个url的连接等待时间 20
            // soTimeout：指的是连接上一个url，获取response的返回等待时间

//            Jedis jedis = new Jedis("188.131.218.191", 6380);
//            jedis.set("Jedis", "Hello Work!");
//            System.out.println(jedis.get("Jedis"));
//            jedis.close();
//            nodes: 188.131.218.191:6380,188.131.218.191:6381,188.131.218.191:6382,188.131.218.191:6383,188.131.218.191:6384,188.131.218.191:6385

            jedisCluster = new JedisCluster(jedisClusterNode, config);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jedisCluster;
        }
    }

}
