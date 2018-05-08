package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginEventHandler extends ImEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginEventHandler.class);

    public LoginEventHandler(ChannelHandlerContext ctx, Msg msg) {
        super(ctx, msg);
    }

    @Override
    public void execute() {
        logger.debug(">>>用户登录事件处理:"+msg.toString());
        //todo 1 认证过程放在这里


        //2 授权成功
        //添加到连接管理容器,并设置userId属性到Channel
        ConnectionManager.addConnection(msg.getUser().getUserId(),ctx);
        //userId 和host主机映射关系 添加到redis
        ChatRoomRedisManager.setUserIdHostRelation(msg.getUser().getUserId());
    }
}
