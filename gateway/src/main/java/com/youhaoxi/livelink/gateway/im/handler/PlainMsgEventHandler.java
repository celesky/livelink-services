package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainMsgEventHandler extends ImEventHandler{
    private static final Logger logger = LoggerFactory.getLogger(PlainMsgEventHandler.class);

    public PlainMsgEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute() {
        PlainUserMsgEvent plainUserMsgEvent = (PlainUserMsgEvent)msg.getEvent();

    }
}
