package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public class TestEventHandler extends IMEventHandler{

    public TestEventHandler(ChannelHandlerContext ctx, Msg msg){
        super( ctx,  msg);
    }

    @Override
    public void execute(Worker worker) {
         worker.dispatcher.dispatch(msg);
    }
}
