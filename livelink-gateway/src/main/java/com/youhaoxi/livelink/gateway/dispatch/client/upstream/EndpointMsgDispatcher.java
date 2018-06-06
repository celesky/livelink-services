package com.youhaoxi.livelink.gateway.dispatch.client.upstream;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.ResultMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.client.DispatchConnManager;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

/**
 * 终端消息派发
 */
public class EndpointMsgDispatcher implements ResultMsgDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(EndpointMsgDispatcher.class);
//    private static final ExecutorService groupDispatchExecutor =
//            new ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());


    /**
     * todo 使用protobuf重新定义
     * @param msg
     */
    @Override
    public void dispatch(EndpointMsg msg) {
        ChannelHandlerContext ctx = DispatchConnManager.getOneConnRamdom();
        //发送到dispatch端
        ctx.writeAndFlush(msg);
    }

    /**
     * todo 使用protobuf重新定义
     * @param msg
     * @param roomId
     */
    @Override
    public void groupDispatch(EndpointMsg msg, String roomId) {
        ChannelHandlerContext ctx = DispatchConnManager.getOneConnRamdom();
        //发送到dispatch端
        ctx.writeAndFlush(msg);
    }

    /**
    @Deprecated
    public void groupDispatch__(ResultMsg msg, String roomId) {
        long startIime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch start:{}",startIime);
        long count = RoomUserRelationSetCache.getRoomMembersCount(roomId);
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
    **/
}
