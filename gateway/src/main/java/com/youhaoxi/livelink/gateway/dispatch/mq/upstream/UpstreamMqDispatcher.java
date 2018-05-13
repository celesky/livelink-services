package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.youhaoxi.livelink.gateway.common.RedisUtil;
import com.youhaoxi.livelink.gateway.dispatch.Dispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.JsonMsgPackUtil;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.UserMsgEvent;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Set;

/**
 * mq广播器 ,派发
 *
 */
public class UpstreamMqDispatcher implements Dispatcher{
    private static final Logger logger = LoggerFactory.getLogger(UpstreamMqDispatcher.class);

    RabbitProducer producer = new RabbitProducer();


    @Override
   public void dispatch(IMsgEvent msg) {

        UserMsgEvent userMsgEvent = (UserMsgEvent)msg;
        //调度
        if(userMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            long startIime =  Instant.now().getEpochSecond();
            logger.info("group chat dispatch start:{}",startIime);
            String roomId = userMsgEvent.getReceiveRoomId();
            long count = ChatRoomRedisManager.getRoomMemnbersCount(roomId);
            int start = 0;
            int stop = 39;
            int range = 40;
            while(count>stop){
                //获取房间成员列表 取20个
                Set<String> set = ChatRoomRedisManager.getRoomMemnbersLimit(roomId,start,stop);
                if(set!=null&&set.size()>0){
                    set.parallelStream().forEach(e->{
                        if(e.contains(":")){
                            String[] arr = e.split(":");
                            Integer userId = Integer.parseInt(arr[0]);
                            String destHost = arr[1];
                            producer.publish(destHost, JsonMsgPackUtil.pack(msg));
                            //logger.info(">>消息群发:roomId:{} ,成员userId:{} ,destHost:{} 已推送mq,msg:{}",roomId,userId,destHost,msg.toString());
                        }else{
                            logger.error("房间roomId:{} members数据格式不正确:{}",roomId,e);
                        }
                    });
                }else{
                    logger.info("房间roomId:{} 没有任何成员,该条消息丢弃 msg:{}",roomId,msg.toString());
                }
                start = start+range;
                stop=stop+range;
            }
            long endTime =  Instant.now().getEpochSecond();
            logger.info("group chat dispatch end:{}",endTime);
            logger.info("group chat dispatch exhaust:{}",(endTime-startIime));
        }else{
            //私聊消息
            //获取对方连接主机的host
            Integer receiverUserId = userMsgEvent.getReceiverUserId();
            String destHost = ChatRoomRedisManager.getUserIdHostRelation(receiverUserId);
            //发到host对应的mq
            logger.info(">>消息私聊: userId:{},destHost:{},已推送mq,msg:{}",receiverUserId,destHost,msg.toString());
            producer.publish(destHost, JsonMsgPackUtil.pack(msg));
        }
    }


    public static void main(String[] args) {
        long count = 100;
        int start = 0;
        int stop = 19;
        int range = 20;
        while(count>stop){

            start = start+range;
            stop=stop+range;

            System.out.println("start = " + start);
            System.out.println("stop = " + stop);
        }
    }
}
