package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import io.netty.channel.ChannelHandlerContext;

public class DemoEventHandler extends IMEventHandler{
    int i=0;
    public DemoEventHandler(ChannelHandlerContext ctx, IMsgEvent msg){
        super( ctx,  msg);
    }


    @Override
    public void execute(IWorker worker) {
        i++;
        if(1==2){
           // System.out.println("i = " + i);
        }
    }
}
