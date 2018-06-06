package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.cache.UserRelationHashCache;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.TestEvent;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

public class TestEventHandler extends IMEventHandler{
    private static final Logger logger = LoggerFactory.getLogger(TestEventHandler.class);

    private static AtomicInteger atomicUserId = new AtomicInteger(1000);
    public static AttributeKey<Integer> AK_USER_ID = AttributeKey.valueOf("userId");


    public TestEventHandler(ChannelHandlerContext ctx, IMsgEvent msg){
        super( ctx,  msg);
    }

    /**
     * 首次连接发送一条连接成功的消息,并生成1个userId放入channelAttributeKey
     * 后续每次发送消息过来 就原文发送回去给自己 并通过派发逻辑发回去 以测试整个派发链路
     * @param worker
     */
    @Override
    public void execute(IWorker worker) {
        long tid = Thread.currentThread().getId();
        logger.info("当前线程id:{} , execute 目前握手数:{} 认证通过:{}",tid,ConnectionManager.channelGroup.size(),ConnectionManager.getCtxMap().size());
        TestEvent msgEvent = (TestEvent)msg;

        //int userId = atomicUserId.getAndIncrement();
        Integer userId  = ctx.channel().attr(AK_USER_ID).get();
        if(userId==null){
            //首次连接,设置一个userId
            userId = msgEvent.getUserId();
            logger.info("threadId:{} , execute  userId:{} 首次登录成功",Thread.currentThread().getId(),userId);
            ctx.channel().attr(AK_USER_ID).set(userId);
            EndpointMsg result = new EndpointMsg(100,userId+"首次登录成功");
            //添加到连接管理容器,并设置userId属性到Channel
            ConnectionManager.addConnection(userId,ctx);
            //userId 和host主机映射关系 添加到redis

            UserRelationHashCache.setUserIdHostRelation(userId, Constants.LOCALHOST);
            //给自己发一条提示消息
            ClientPushUtil.writeToClient(ctx,result,userId);
        }

        //再通过整个链路发一条消息给自己
        EndpointMsg rmsg = new EndpointMsg(10,msgEvent.msgContent);
        rmsg.setFrom(msgEvent.getFrom());
        rmsg.setDest(new User()
                .setUserId(userId));
        rmsg.setTimestamp(msgEvent.getHeader().getDateTime().toEpochSecond(ZoneOffset.of("+8")));//设置时间
        worker.getDispatcher().dispatch(rmsg);

    }


    /**
     * 压力测试方法 生成一个序列号当userId 以标识这个链接
     * @param ctx
     */
    private void pressureTest(ChannelHandlerContext ctx){


    }
}
