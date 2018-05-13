package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.UpstreamMqDispatcher;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainMsgEventHandler extends IMEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlainMsgEventHandler.class);

    private UpstreamMqDispatcher dispatcher;

    public PlainMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        logger.debug(">>>用户普通消息事件处理:"+msg.toString());
        //PlainUserMsgEvent plainMsgEvent = (PlainUserMsgEvent)msg;
        //PlainUserMsgEvent plainUserMsgEvent = (PlainUserMsgEvent)msg.getEvent();
        woker.dispatcher.dispatch(msg);
    }
}
