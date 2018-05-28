package com.youhaoxi.livelink.gateway.common.util;

import com.huisa.common.cache.redis.JedisCache;
import com.huisa.common.cache.redis.assist.Config_JedisCache;
import com.youhaoxi.livelink.gateway.common.ConfigPropertes;
import com.youhaoxi.livelink.gateway.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil {

    @Autowired
    private  ConfigPropertes configPropertes;

    private  JedisCache cache;

    private static RedisUtil redisUtil;

    @PostConstruct
    public void init(){
        redisUtil = this;
    }

    public static synchronized void initCache() {
        // 初始化redis
        //String redisServers = sysconfigService.getValue("REDIS_SERVERS");
        if(redisUtil.cache ==null){
            String redisServers = redisUtil.configPropertes.REDIS_SERVER;
            Config_JedisCache jedisCacheConfig = new Config_JedisCache(redisServers, null);
            jedisCacheConfig.setMaxTotal(1000);
            jedisCacheConfig.setMaxIdle(30);
            jedisCacheConfig.setMaxWaitMillis(1000);
            jedisCacheConfig.setTestOnBorrow(true);
            try {
                redisUtil.cache = new JedisCache(jedisCacheConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static JedisCache cache(){
        if(redisUtil.cache==null){
            initCache();
        }
        return redisUtil.cache;
    }

    public static void main(String[] args) {
        RedisUtil.cache().set("test","hello");
        System.out.println("args = " + RedisUtil.cache().get("test"));
    }



}
