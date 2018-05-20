package com.youhaoxi.livelink.gateway.dispatch.mq.inter.upstream;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.InterMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqInterMsgDispatcher implements InterMsgDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MqInterMsgDispatcher.class);
//    private static final ExecutorService groupDispatchExecutor =
//            new ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    //fanout类型 广播给所有服务节点
    RabbitProducer producer = new RabbitProducer(Constants.INTER_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

    @Override
    public void dispatch(InterMsg msg) {
         producer.publish("", JSON.toJSONString(msg));
    }


}
