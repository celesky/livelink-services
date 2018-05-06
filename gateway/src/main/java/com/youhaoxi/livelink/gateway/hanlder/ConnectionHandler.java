package com.youhaoxi.livelink.gateway.hanlder;

import com.youhaoxi.livelink.gateway.bean.event.LoginEvent;
import com.youhaoxi.livelink.gateway.bean.event.LogoutEvent;
import com.youhaoxi.livelink.gateway.bean.msg.UserMsg;
import com.youhaoxi.livelink.gateway.util.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责连接管理
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    /**
     *  first
     * @param ctx
     * @throws Exception
     */
   @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //从自定义容器中删除连接对象
        ConnectionManager.closeConnection(ctx);
    }

    /**
     * second
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果是握手成功事件，则从该 Channelipeline 中移除 Http- RequestHandler 因为将不会 接收到任何 HTTP 消息了
        if (evt == WebSocketServerProtocolHandler
                .ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {

            ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            //添加到连接组
            ConnectionManager.channelGroup.add(ctx.channel());

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * third
     * 收到消息事件
     * authorizeHandler已经处理了权限,进入到这里的认为是合法的连接
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //ConnectionManager.channelGroup.writeAndFlush(bean.retain());
        UserMsg userMsg = (UserMsg)msg;
        //登录事件
        if(userMsg.event instanceof LoginEvent){
            logger.debug(">>>用户登录事件:"+userMsg.toString());
            //添加到连接管理容器,并设置userId属性到Channel
            ConnectionManager.addConnection(userMsg.user.getUserId(),ctx);
            //userId 和host主机映射关系 添加到redis
            ChatRoomRedisManager.setUserIdHostRelation(userMsg.user.getUserId());
        }else if(userMsg.event instanceof LogoutEvent){
            logger.debug(">>>用户登出事件:"+userMsg.toString());
            ConnectionManager.closeConnection(ctx);
            ChatRoomRedisManager.removeUserIdHostRelation(userMsg.user.getUserId());
        } else {
            ctx.fireChannelRead(userMsg);
        }

    }


}
