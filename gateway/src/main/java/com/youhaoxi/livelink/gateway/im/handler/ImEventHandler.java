package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public abstract class ImEventHandler implements EventHandler{
    protected ChannelHandlerContext ctx;
    protected Msg msg;


    public ImEventHandler(ChannelHandlerContext ctx,Msg msg){
        this.ctx=ctx;
        this.msg=msg;
    }



}
