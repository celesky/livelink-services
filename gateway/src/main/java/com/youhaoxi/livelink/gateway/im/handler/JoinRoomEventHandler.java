package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.event.JoinRoomEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * 加入聊天室事件处理
 */
public class JoinRoomEventHandler  extends ImEventHandler {

    public JoinRoomEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute() {
        JoinRoomEvent event = (JoinRoomEvent)msg.getEvent();
        ChatRoomRedisManager.addUserToRoom(event.getUserId(),event.getRoomId());
    }
}
