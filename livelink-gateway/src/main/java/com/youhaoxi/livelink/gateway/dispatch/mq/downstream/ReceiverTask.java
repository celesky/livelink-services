package com.youhaoxi.livelink.gateway.dispatch.mq.downstream;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConsumer;

public class ReceiverTask implements Runnable{

    RabbitConsumer rabbitConsumer = new RabbitConsumer();
    String routeKey = Constants.LOCALHOST;
    Sender sender = null;


//    private static ExecutorService executorService =
//            new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    public ReceiverTask(Sender sender){
        this.sender = sender;
    }

    public ReceiverTask(String routeKey, Sender sender){
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
