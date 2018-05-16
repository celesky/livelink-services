package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import io.netty.channel.ChannelHandlerContext;

public class TestEventHandler extends IMEventHandler{

    public TestEventHandler(ChannelHandlerContext ctx, IMsgEvent msg){
        super( ctx,  msg);
    }

    @Override
    public void execute(Worker worker) {
         //worker.dispatcher.dispatch(msg);
    }
}
