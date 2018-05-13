package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.JoinRoomEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * 加入聊天室事件处理
 */
public class JoinRoomEventHandler  extends IMEventHandler {

    public JoinRoomEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        JoinRoomEvent event = (JoinRoomEvent)msg;
        ChatRoomRedisManager.addUserToRoom(event.getUserId(),event.getRoomId());
    }
}
