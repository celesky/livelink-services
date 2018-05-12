package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.msg.IMsg;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public abstract class IMEventHandler implements EventHandler{
    protected ChannelHandlerContext ctx;
    protected Msg msg;


    public IMEventHandler(ChannelHandlerContext ctx, Msg msg){
        this.ctx=ctx;
        this.msg=msg;
    }



}
