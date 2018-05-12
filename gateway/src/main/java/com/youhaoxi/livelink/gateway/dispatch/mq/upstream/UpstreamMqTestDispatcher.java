package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.Dispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.UserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.IMsg;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpstreamMqTestDispatcher implements Dispatcher {

    private static final Logger logger = LoggerFactory.getLogger(UpstreamMqDispatcher.class);

    RabbitProducer producer = new RabbitProducer();

    @Override
    public void dispatch(IMsg iMsg) {

        //Msg msg = (Msg)iMsg;
        //UserMsgEvent plainUserMsgEvent = (UserMsgEvent)msg.event;

        //私聊消息
        //获取对方连接主机的host
        Integer receiverUserId = 111;
        String destHost = Constants.RABBIT_HOST;
        //发到host对应的mq
        String msg = "hello,你好 fuckyoueveryworld!";
        //logger.info(">>消息私聊: "+msg+" "+receiverUserId);

        producer.publish(destHost,msg);
    }
}
