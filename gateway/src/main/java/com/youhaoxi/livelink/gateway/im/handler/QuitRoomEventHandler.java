package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.event.QuitRoomEvent;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitRoomEventHandler extends IMEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(QuitRoomEventHandler.class);

    public QuitRoomEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        logger.debug(">>>用户退出聊天室消息事件处理:"+msg.toString());
        QuitRoomEvent event = (QuitRoomEvent)msg.getEvent();
        ChatRoomRedisManager.removeUserFromRoom(event.getUserId(),event.getRoomId());

    }
}
