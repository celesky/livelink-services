package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public class QuitRoomEventHandler extends ImEventHandler{
    public QuitRoomEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute() {

    }
}
