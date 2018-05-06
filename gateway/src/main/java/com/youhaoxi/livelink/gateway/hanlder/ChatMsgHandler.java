package com.youhaoxi.livelink.gateway.hanlder;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.bean.event.JoinRoomEvent;
import com.youhaoxi.livelink.gateway.bean.event.PlainMsgEvent;
import com.youhaoxi.livelink.gateway.bean.event.QuitRoomEvent;
import com.youhaoxi.livelink.gateway.bean.event.RichMsgEvent;
import com.youhaoxi.livelink.gateway.dispatch.upstream.BroadToMqTask;
import com.youhaoxi.livelink.gateway.bean.msg.UserMsg;
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
public class ChatMsgHandler extends SimpleChannelInboundHandler<UserMsg> {
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
    protected void channelRead0(ChannelHandlerContext ctx, UserMsg msg) throws Exception {
        //ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame(jsonMsg));
        logger.debug(">>>聊天信息处理:"+JSONObject.toJSONString(msg));
        //聊天状态处理

        if(msg.event instanceof JoinRoomEvent){ //加入聊天室
            JoinRoomEvent event = (JoinRoomEvent)msg.event;
            ChatRoomRedisManager.addUserToRoom(event.getUserId(),event.getRoomId());

        }else if(msg.event instanceof QuitRoomEvent){//退出聊天室
            QuitRoomEvent event = (QuitRoomEvent)msg.event;
            ChatRoomRedisManager.removeUserFromRoom(event.getUserId(),event.getRoomId());
            //退出聊天室后关闭连接
        }else if(msg.event instanceof PlainMsgEvent){//普通文本消息
            //需要路由广播
            Future<Integer> future = executorService.submit(new BroadToMqTask(msg));
        }else if(msg.event instanceof RichMsgEvent){//道具或礼物消息
            //需要路由广播
            Future<Integer> future = executorService.submit(new BroadToMqTask(msg));
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
