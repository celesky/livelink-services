package com.youhaoxi.livelink.gateway.dispatch.mq.upstream;

import com.youhaoxi.livelink.gateway.cache.RoomUserRelationSetCache;
import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.util.StringUtils;
import com.youhaoxi.livelink.gateway.dispatch.EventDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.JsonEventPackUtil;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.UserMsgEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.*;

/**
 * mq广播器 ,派发
 * 广播原始事件
 */
public class MqEventDispatcher implements EventDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MqEventDispatcher.class);
    private static final ExecutorService groupDispatchExecutor =
            new ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    RabbitProducer producer = new RabbitProducer();


    //私聊消息
    @Override
   public void dispatch(IMsgEvent msg) {
        long startIime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch start:{}",startIime);
        UserMsgEvent userMsgEvent = (UserMsgEvent)msg;

        //获取对方连接主机的host
        Integer receiverUserId = userMsgEvent.getReceiverUserId();
        String destHost = UserRelationHashCache.getUserIdHostRelation(receiverUserId);
        if(!StringUtils.isBlank(destHost)){
            //发到host对应的mq
            logger.info(">>消息私聊: userId:{},destHost:{},已推送mq,msg:{}",receiverUserId,destHost,msg.toString());
            producer.publish(destHost, JsonEventPackUtil.pack(msg));
        }else{
            logger.info(">>消息私聊: userId:{},destHost:{},用户不在线,msg:{}",receiverUserId,destHost,msg.toString());

        }

//        Long lastTime = list.stream().map(e-> {
//            try {
//                return e.get();
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            } catch (ExecutionException e1) {
//                e1.printStackTrace();
//            }
//            return 0L;
//        }).max(Long::compare).get();
//
//        logger.info("lastTime = " + lastTime);
//        logger.info("group chat dispatch exhaust:{}",(lastTime-startIime));

    }

    @Override
    public void groupDispatch(IMsgEvent msg, String roomId) {
        long startIime =  Instant.now().getEpochSecond();
        logger.info("group chat dispatch start:{}",startIime);
        UserMsgEvent userMsgEvent = (UserMsgEvent)msg;
        long count = RoomUserRelationSetCache.getRoomMemnbersCount(roomId);
        int start = 0;
        int stop = 199;
        int range = 200;
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

    private void dispatchWithLimit(String roomId,int start,int stop,IMsgEvent msg) {
        Set<String> set = RoomUserRelationSetCache.getRoomMemnbersLimit(roomId,start,stop);
        if(set!=null&&set.size()>0){
            set.stream().parallel().forEach(e->{
                if(e.contains(":")){
                    String[] arr = e.split(":");
                    Integer userId = Integer.parseInt(arr[0]);
                    String destHost = arr[1];
                    producer.publish(destHost, JsonEventPackUtil.pack(msg));
                    //logger.info(">>消息群发:roomId:{} ,成员userId:{} ,destHost:{} 已推送mq,msg:{}",roomId,userId,destHost,msg.toString());
                }else{
                    logger.error("房间roomId:{} members数据格式不正确:{}",roomId,e);
                }
            });
        }else{
            logger.info("房间roomId:{} 没有任何成员,该条消息丢弃 msg:{}",roomId,msg.toString());
        }
    }




    class groupDspTask implements Callable<Long>{
        String roomId;
        int start;
        int stop;
        IMsgEvent msg;

        public groupDspTask(String roomId,int start,int stop,IMsgEvent msg){
            this.roomId = roomId;
            this.start = start;
            this.stop = stop;
            this.msg = msg;
        }

        @Override
        public Long call() throws Exception {

            dispatchWithLimit( roomId, start, stop, msg);
            return Instant.now().getEpochSecond();
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
