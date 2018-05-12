package com.youhaoxi.livelink.gateway.dispatch.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 每个线程1个producer,对应1个channel
 */
public class RabbitProducer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    private static final String EXCHANGE_NAME = Constants.EXCHANGE_NAME;

    private Channel channel = RabbitConnectionManager.getInstance().getNewChannel();

    public RabbitProducer(){
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        } catch (IOException e) {
            logger.error("RabbitProducer error:"+e.getMessage(),e);
        }
    }

    public  void publish(String routeKey,Msg msg){
        try {
            String json = JSON.toJSONString(msg);
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, json.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.error("publish error:"+e.getMessage(),e);
        }
    }
    public  void publish(String routeKey,String msg){
        try {
            //String json = JSON.toJSONString(msg);
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes("UTF-8"));
        } catch (IOException e) {
            logger.error("publish error:"+e.getMessage(),e);
        }
    }


    public static void main(String[] argv) throws Exception {


    }


    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) return "";
        if (length < startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
