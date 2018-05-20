package com.youhaoxi.livelink.gateway.dispatch.mq.inter.downstream;

import com.rabbitmq.client.BuiltinExchangeType;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConsumer;
import com.youhaoxi.livelink.gateway.dispatch.mq.Processor;

/**
 * 订阅所有内部消息,用来服务间同步状态
 */
public class InterMsgReceiverTask implements Runnable{

    RabbitConsumer rabbitConsumer = new RabbitConsumer(Constants.INTER_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    String routeKey="";
    Processor processor ;


//    private static ExecutorService executorService =
//            new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    public InterMsgReceiverTask(Processor processor){
        this.processor = processor;
    }

    public InterMsgReceiverTask(String routeKey, Processor processor){
        this.routeKey = routeKey;
        this.processor = processor;
    }

    @Override
    public void run() {
        rabbitConsumer.setProcessor(processor);
        //这里会阻塞和挂起
        rabbitConsumer.consume(routeKey);
    }




}
