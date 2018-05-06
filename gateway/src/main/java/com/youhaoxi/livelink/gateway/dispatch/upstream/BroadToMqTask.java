package com.youhaoxi.livelink.gateway.dispatch.upstream;

import com.youhaoxi.livelink.gateway.bean.enums.BroadType;
import com.youhaoxi.livelink.gateway.bean.event.PlainMsgEvent;
import com.youhaoxi.livelink.gateway.bean.event.RichMsgEvent;
import com.youhaoxi.livelink.gateway.bean.msg.UserMsg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 广播消息到相应的mq队列
 * 返回结果编码
 */
public class BroadToMqTask implements Callable<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(BroadToMqTask.class);

    UserMsg userMsg;
    public BroadToMqTask(){

    }
    public BroadToMqTask(UserMsg userMsg){
        this.userMsg = userMsg;
    }
    @Override
    public Integer call() throws Exception {

        if(userMsg.event instanceof PlainMsgEvent){//普通消息
            PlainMsgEvent plainMsgEvent = (PlainMsgEvent)userMsg.event;
            //群发消息, 用户可能没加入到这个群
            if(plainMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
                String roomId = plainMsgEvent.getReceiveRoomId();
                //获取房间成员列表
                //RedisUtil.cache().z
                logger.info("plainMsgEvent:"+plainMsgEvent.toString()+" 已经广播到了"+roomId+" 房间");
            }else{
                //私聊消息
                //获取对方连接主机的host
                Integer receiverUserId = plainMsgEvent.getReceiverUserId();
                String host = ChatRoomRedisManager.getUserIdHostRelation(receiverUserId);
                //发到host对应的mq
                logger.info("plainMsgEvent:"+plainMsgEvent.toString()+" 发送给了用户"+receiverUserId);
            }


        }else if(userMsg.event instanceof RichMsgEvent){//道具礼物消息
            RichMsgEvent plainMsgEvent = (RichMsgEvent)userMsg.event;
        }
        return null;
    }
}
