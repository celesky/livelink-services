package com.youhaoxi.livelink.gateway.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigPropertes {

    @Value("${rabbit.host:47.106.140.44}")
    public  String RABBIT_HOST;
    //rabbitMq
    @Value("${rabbit.port:5672}")
    public int RABBIT_PORT;

    @Value("${rabbit.exchange_name:direct_chat}")
    public String EXCHANGE_NAME ;


    //redis
    @Value("${redis.server:47.106.140.44:5354}")
    public String REDIS_SERVER;


    //dispatch worker num
    @Value("${dispatcher.workerNum:4}")
    public int WORK_NUM =4;


    @Value("${server.port:8080}")
    public int SERVER_PORT =4;


    @Value("${server.channel_type:NIO}")
    public String CHANNEL_TYPE;

}
