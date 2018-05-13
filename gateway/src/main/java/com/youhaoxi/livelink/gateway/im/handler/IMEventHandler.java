package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.IMsg;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public abstract class IMEventHandler implements EventHandler{
    protected ChannelHandlerContext ctx;
    protected IMsgEvent msg;


    public IMEventHandler(ChannelHandlerContext ctx, IMsgEvent msg){
        this.ctx=ctx;
        this.msg=msg;
    }



}
