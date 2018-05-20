package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.im.enums.InterMsgType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.LogoutEvent;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutEventHandler extends IMEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutEventHandler.class);

    public LogoutEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(IWorker woker) {
        logger.debug(">>>用户登出事件:"+msg.toString());
        LogoutEvent logoutEvent =  (LogoutEvent)msg;
        ConnectionManager.closeConnection(ctx);
        //UserRelationHashCache.removeUserIdHostRelation(logoutEvent.from.getUserId());
        //redis中清除
        ChatRoomRedisManager.clearUserIdCacheData(logoutEvent.from.getUserId());

        //传播断开连接事件给其他节点服务,同步用户下线状态
        InterMsg interMsg = new InterMsg();
        interMsg.setHost(Constants.LOCALHOST)
                .setInterMsgType(InterMsgType.unlinked)
                .setUserId(logoutEvent.from.getUserId());
        woker.getInterMsgDispatcher().dispatch(interMsg);
    }
}
