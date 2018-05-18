package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.RichUserMsgEvent;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 广播消息到相应的mq队列
 * 返回结果编码
 */
public class UpToMqTask implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UpToMqTask.class);

    IMsgEvent msg;

    RabbitProducer producer = new RabbitProducer();

    public UpToMqTask(){

    }
    public UpToMqTask(IMsgEvent msg){
        this.msg = msg;
    }
    @Override
    public Integer call() throws Exception {

        if(msg instanceof PlainUserMsgEvent){//普通消息
            PlainUserMsgEvent plainUserMsgEvent = (PlainUserMsgEvent) msg;
            //群发消息, 用户可能没加入到这个群,需要前置校验下
            if(plainUserMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
                String roomId = plainUserMsgEvent.getReceiveRoomId();
                //获取房间成员列表
                //RedisUtil.cache().z
                logger.info(">>普通消息群发:"+ plainUserMsgEvent.toString()+" 已经广播到了"+roomId+" 房间");
            }else{
                //私聊消息
                //获取对方连接主机的host
                Integer receiverUserId = plainUserMsgEvent.getReceiverUserId();
                String host = UserRelationHashCache.getUserIdHostRelation(receiverUserId);
                //发到host对应的mq
                logger.info(">>普通消息私聊:"+ plainUserMsgEvent.toString()+" 发送给了用户"+receiverUserId);
                //producer.publish(host, msg);

            }
        }else if(msg instanceof RichUserMsgEvent){//道具礼物消息
            RichUserMsgEvent plainMsgEvent = (RichUserMsgEvent) msg;
        }
        return null;
    }
}
