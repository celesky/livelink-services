package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.ResultMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Set;

public class MqRstMsgDispatcher implements ResultMsgDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MqRstMsgDispatcher.class);
//    private static final ExecutorService groupDispatchExecutor =
//            new ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    RabbitProducer producer = new RabbitProducer();


    @Override
    public void dispatch(ResultMsg msg) {
        long startIime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch start:{}",startIime);

        //获取对方连接主机的host
        Integer receiverUserId = msg.dest.userId;
        String destHost = UserRelationHashCache.getUserIdHostRelation(receiverUserId);
        if(!StringUtils.isBlank(destHost)){
            //如果没有时间 则填上当前时间
            if(msg.getTimestamp()==null||msg.getTimestamp().intValue()==0){
                msg.setTimestamp(Instant.now().toEpochMilli());
            }
            //发到host对应的mq
            logger.info(">>消息私聊: userId:{},destHost:{},已推送mq,msg:{}",receiverUserId,destHost,msg.toString());
            producer.publish(destHost, JSON.toJSONString(msg));
        }else{
            logger.info(">>消息私聊: userId:{},destHost:{},用户不在线,msg:{}",receiverUserId,destHost,msg.toString());

        }
    }

    @Override
    public void groupDispatch(ResultMsg msg, String roomId) {
        long startIime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch start:{}",startIime);
        long count = RoomUserRelationSetCache.getRoomMemnbersCount(roomId);
        int start = 0;
        int stop = 199;
        int range = 200;

        msg.setRoomId(roomId);
        do{
            //线程池派发处理
            //Future<Long> future = groupDispatchExecutor.submit(new groupDspTask( roomId, start, stop, msg));
            //list.add(future);
            dispatchWithLimit( roomId, start, stop, msg);
            start = start+range;
            stop=stop+range;

        }while(count-1>stop);

        long endTime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch end:{}",endTime);
        logger.info("group chat dispatch exhaust:{}",(endTime-startIime));
    }

    private void dispatchWithLimit(String roomId,int start,int stop,ResultMsg msg) {
        Set<String> set = RoomUserRelationSetCache.getRoomMemnbersLimit(roomId,start,stop);
        if(set!=null&&set.size()>0){
            set.stream().parallel().forEach(e->{
                Integer userId = Integer.parseInt(e);
                //用户连接在的主机
                String destHost = UserRelationHashCache.getUserIdHostRelation(userId);

                //设置接收目标的userId
                if(msg.getDest()==null){
                    User dest = new User();
                    dest.setUserId(userId);
                    msg.setDest(dest);
                }else{
                    msg.getDest().setUserId(userId);
                }

                //如果没有时间 则填上当前时间
                if(msg.getTimestamp()==null||msg.getTimestamp().intValue()==0){
                    msg.setTimestamp(Instant.now().toEpochMilli());
                }

                String json = JSON.toJSONString(msg);
                producer.publish(destHost, json);
                //logger.info(">>消息群发:roomId:{} ,成员userId:{} ,destHost:{} 已推送mq,msg:{}",roomId,userId,destHost,msg.toString());

            });
        }else{
            logger.info("房间roomId:{} 没有任何成员,该条消息丢弃 msg:{}",roomId,msg.toString());
        }
    }
}
