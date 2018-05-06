package com.youhaoxi.livelink.gateway.common;

import com.huisa.common.cache.redis.JedisCache;
import com.huisa.common.cache.redis.assist.Config_JedisCache;

public class RedisUtil {

    private static JedisCache cache;


    public static void init() {
        // 初始化redis
        //String redisServers = sysconfigService.getValue("REDIS_SERVERS");
        String redisServers = Constants.REDIS;
        Config_JedisCache jedisCacheConfig = new Config_JedisCache(redisServers, null);
        jedisCacheConfig.setMaxTotal(10);
        jedisCacheConfig.setMaxIdle(30);
        jedisCacheConfig.setMaxWaitMillis(1000);
        jedisCacheConfig.setTestOnBorrow(true);
        try {
            cache = new JedisCache(jedisCacheConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JedisCache cache(){
        if(cache==null){
            init();
        }
        return cache;
    }

    public static void main(String[] args) {
        RedisUtil.cache().set("test","hello");
        System.out.println("args = " + RedisUtil.cache().get("test"));
    }
}
