package com.youhaoxi.livelink.gateway.handler;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.im.event.*;
import com.youhaoxi.livelink.gateway.im.event.JoinRoomEvent;
import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 聊天消息处理
 */
public class ChatMsgHandler extends SimpleChannelInboundHandler<Msg> {
    private static final Logger logger = LoggerFactory.getLogger(ChatMsgHandler.class);
    private static final ExecutorService executorService = new ThreadPoolExecutor(8,
    16,
    60,
    TimeUnit.SECONDS,
    new LinkedBlockingDeque<>(1000));

    /**
     * JOINROOM(2), PLAINMSG(3), RICHMSG( 4),QUITROOM(5),LOGOUT(6)
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        //ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame(jsonMsg));
        logger.debug(">>>聊天信息处理:"+JSONObject.toJSONString(msg));
        //聊天状态处理



        if(msg.getEvent() instanceof JoinRoomEvent){ //加入聊天室
            JoinRoomEvent event = (JoinRoomEvent)msg.getEvent();
            ChatRoomRedisManager.addUserToRoom(event.getUserId(),event.getRoomId());

        }else if(msg.getEvent() instanceof QuitRoomEvent){//退出聊天室
            QuitRoomEvent event = (QuitRoomEvent)msg.getEvent();
            ChatRoomRedisManager.removeUserFromRoom(event.getUserId(),event.getRoomId());
            //退出聊天室后关闭连接
        }else if(msg.getEvent() instanceof PlainUserMsgEvent){//普通文本消息
            //需要路由广播
            //Future<Integer> future = executorService.submit(new BroadToMqTask(msg));
        }else if(msg.getEvent() instanceof RichUserMsgEvent){//道具或礼物消息
            //需要路由广播
            //Future<Integer> future = executorService.submit(new BroadToMqTask(msg));
        }else {
            logger.error("未知消息类型:"+msg.toString());
        }


    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("报错了!!!!!!!!!!!!!!");
        cause.printStackTrace();
        ConnectionManager.closeConnection(ctx);
    }


}
