package com.youhaoxi.livelink.gateway.dispatch.mq.im.downstream;

import com.rabbitmq.client.BuiltinExchangeType;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConsumer;
import com.youhaoxi.livelink.gateway.dispatch.mq.Processor;

/**
 * 收听所有转发过来的聊天消息,并用sender发送给用户端
 */
public class ChatMsgReceiverTask implements Runnable{

    RabbitConsumer rabbitConsumer = new RabbitConsumer(Constants.CHAT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
    String routeKey = Constants.LOCALHOST;
    Processor sender ;


//    private static ExecutorService executorService =
//            new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    public ChatMsgReceiverTask(Processor sender){
        this.sender = sender;
    }

    public ChatMsgReceiverTask(String routeKey, Processor sender){
        this.routeKey = routeKey;
        this.sender = sender;
    }

    @Override
    public void run() {
        rabbitConsumer.setProcessor(sender);
        //这里会阻塞和挂起
        rabbitConsumer.consume(routeKey);
    }




}
