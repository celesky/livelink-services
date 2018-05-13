package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.UpstreamMqDispatcher;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;

public class RichMsgEventHandler extends IMEventHandler {

    private UpstreamMqDispatcher dispatcher;

    public RichMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {

        woker.dispatcher.dispatch(msg);
    }
}
