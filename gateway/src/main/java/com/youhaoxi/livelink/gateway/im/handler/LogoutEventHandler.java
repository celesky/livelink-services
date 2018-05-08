package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutEventHandler extends ImEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutEventHandler.class);

    public LogoutEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute() {
        logger.debug(">>>用户登出事件:"+msg.toString());
        ConnectionManager.closeConnection(ctx);
        ChatRoomRedisManager.removeUserIdHostRelation(msg.getUser().getUserId());
    }
}
