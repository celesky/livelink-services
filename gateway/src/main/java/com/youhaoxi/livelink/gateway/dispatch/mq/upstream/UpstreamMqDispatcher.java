package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.youhaoxi.livelink.gateway.dispatch.Dispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.UserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.IMsg;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mq广播器 ,派发
 *
 */
public class UpstreamMqDispatcher implements Dispatcher{
    private static final Logger logger = LoggerFactory.getLogger(UpstreamMqDispatcher.class);

    RabbitProducer producer = new RabbitProducer();


    @Override
   public void dispatch(IMsg iMsg) {

        Msg msg = (Msg)iMsg;
        UserMsgEvent plainUserMsgEvent = (UserMsgEvent)msg.event;
        //调度
        if(plainUserMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            String roomId = plainUserMsgEvent.getReceiveRoomId();
            //获取房间成员列表
            //RedisUtil.cache().z
            logger.info(">>消息群发:"+ plainUserMsgEvent.toString()+" 已经广播到了"+roomId+" 房间");
        }else{
            //私聊消息
            //获取对方连接主机的host
            Integer receiverUserId = plainUserMsgEvent.getReceiverUserId();
            String destHost = ChatRoomRedisManager.getUserIdHostRelation(receiverUserId);
            //发到host对应的mq
            logger.info(">>消息私聊:"+ plainUserMsgEvent.toString()+" 发送给了用户"+receiverUserId);
            producer.publish(destHost,msg);
        }
    }
}
