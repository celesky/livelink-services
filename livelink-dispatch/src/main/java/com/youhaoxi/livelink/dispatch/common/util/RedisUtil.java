package com.youhaoxi.livelink.dispatch.common.util;

import com.youhaoxi.base.jedis.client.RedisCluster;
import com.youhaoxi.livelink.dispatch.common.ConfigPropertes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil {

    @Autowired
    private ConfigPropertes configPropertes;

    private static RedisCluster cache = new RedisCluster();

    private static RedisUtil redisUtil;

    @PostConstruct
    public void init(){
        redisUtil = this;
    }

    public static RedisCluster cache(){
        return cache;
    }



    public static void main(String[] args) {
        RedisUtil.cache().set("test","hello");
        System.out.println("args = " + RedisUtil.cache().get("test"));
    }



}
