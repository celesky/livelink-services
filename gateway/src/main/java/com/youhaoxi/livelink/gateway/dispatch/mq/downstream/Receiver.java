package com.youhaoxi.livelink.gateway.dispatch.mq.downstream;

import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Receiver extends Thread{

    RabbitConsumer rabbitConsumer = new RabbitConsumer();
    String routeKey = null;
    Sender sender = null;


//    private static ExecutorService executorService =
//            new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    public Receiver(String routeKey,Sender sender){
        this.routeKey = routeKey;
        this.sender = sender;
    }

    @Override
    public void run() {
        rabbitConsumer.setSender(sender);
        //这里会阻塞和挂起
        rabbitConsumer.consume(routeKey);
    }




}
