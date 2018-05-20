package com.youhaoxi.livelink.gateway.dispatch.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.youhaoxi.livelink.gateway.common.ConfigPropertes;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class RabbitConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(RabbitConnectionManager.class);
    //private static RabbitConnectionManager instance = new RabbitConnectionManager();
    private static ConnectionFactory factory = new ConnectionFactory();
    private static Connection connection;

    private static List<Channel> channels = new ArrayList<>();


    private static ReentrantLock resLock = new ReentrantLock();

    //延迟初始化 单例模式
    private static class SingletonHolder{
        private  static RabbitConnectionManager instance = new RabbitConnectionManager();
    }

    public static RabbitConnectionManager getInstance(){
        return SingletonHolder.instance;
    }

    private RabbitConnectionManager(){
        init();
    }



    private void initFactory(){
        try {
            factory = new ConnectionFactory();
            ConfigPropertes configPropertes = ApplicationContextUtil.getBean(ConfigPropertes.class);
            factory.setHost(configPropertes.RABBIT_HOST);
            factory.setPort(configPropertes.RABBIT_PORT);
        }catch (Exception e){
            logger.error("RabbitConnectionManager init error:"+e.getMessage(),e);
        }
    }

    private  Connection initConnection(){
        try {
            if(connection!=null){
                try{
                    connection.close();
                }catch(Exception e){
                    logger.warn("initConnection 前先调用connection.close() ,但是报错了,可以不检查这个问题");
                }
            }
            connection = factory.newConnection();
        } catch (IOException e) {
            logger.error("RabbitConnectionManager init error:"+e.getMessage(),e);
        } catch (TimeoutException e) {
            logger.error("RabbitConnectionManager init timeout:"+e.getMessage(),e);
        }


        return connection;
    }

    public  synchronized void init(){
        initFactory();
        initConnection();
    }

    /**
     * 获取一个RabbitChannel
     * @return
     */
    public  Channel getNewChannel(){
        try{
            resLock.lock();
            if(connection==null||!connection.isOpen()){
                logger.error(">>>coonnection为空 , 重新初始化rabbitmq的factory和connection..........");
                //如果工厂为空 先初始化工厂
                if(factory==null){
                    initFactory();
                }
                //初始化连接
                initConnection();
            }
            Channel channel = null;
            try {
                channel = connection.createChannel();
                channels.add(channel);//添加到channelList
            } catch(Exception e){
                logger.error(">>>RabbitConnectionManager getNewChannel Exception:"+e.getMessage(),e);
            }
            return channel;
        }finally {
            resLock.unlock();
        }
    }

    public void closeConnection(){
        try {
            if(connection!=null){
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeChannel(Channel channel){
        try {
            if(channel!=null){
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
