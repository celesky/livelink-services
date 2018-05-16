package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.ResultMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
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
        String json = JSON.toJSONString(msg);
        do{
            //线程池派发处理
            //Future<Long> future = groupDispatchExecutor.submit(new groupDspTask( roomId, start, stop, msg));
            //list.add(future);
            dispatchWithLimit( roomId, start, stop, json);
            start = start+range;
            stop=stop+range;

        }while(count-1>stop);

        long endTime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch end:{}",endTime);
        logger.info("group chat dispatch exhaust:{}",(endTime-startIime));
    }

    private void dispatchWithLimit(String roomId,int start,int stop,String json) {
        Set<String> set = RoomUserRelationSetCache.getRoomMemnbersLimit(roomId,start,stop);
        if(set!=null&&set.size()>0){
            set.stream().parallel().forEach(e->{
                if(e.contains(":")){
                    String[] arr = e.split(":");
                    Integer userId = Integer.parseInt(arr[0]);
                    String destHost = arr[1];
                    producer.publish(destHost, json);
                    //logger.info(">>消息群发:roomId:{} ,成员userId:{} ,destHost:{} 已推送mq,msg:{}",roomId,userId,destHost,msg.toString());
                }else{
                    logger.error("房间roomId:{} members数据格式不正确:{}",roomId,e);
                }
            });
        }else{
            logger.info("房间roomId:{} 没有任何成员,该条消息丢弃 msg:{}",roomId,json);
        }
    }
}
