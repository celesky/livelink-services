package com.youhaoxi.livelink.gateway.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.dispatch.work.DisruptorWorker;
import com.youhaoxi.livelink.gateway.dispatch.work.Worker;
import com.youhaoxi.livelink.gateway.im.event.LogoutEvent;
import com.youhaoxi.livelink.gateway.im.handler.HandlerManager;
import com.youhaoxi.livelink.gateway.im.handler.IMEventHandler;
import com.youhaoxi.livelink.gateway.im.msg.User;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.*;
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

    /**
     * 连接断开事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //ConnectionManager.channelGroup.remove(ctx);//手动移除  其自身也有listener可以自动移除
        //从自定义容器中删除连接对象
        ConnectionManager.closeConnection(ctx);
        Integer userId = ConnectionManager.getUserIdInCtx(ctx);

        logger.info(">>>netty channelInactive 连接断开事件: 当前channelGroup:"+ConnectionManager.channelGroup.size());

//        if(userId!=null){
//            //UserRelationHashCache.removeUserIdHostRelation(userId);
//            ChatRoomRedisManager.clearUserIdCacheData(userId);
//
//            //和用户logout事件同等处理 这里主动生成一个logoutEvent,派发给worker去处理
//            LogoutEvent msg = new LogoutEvent();
//            msg.setFrom(new User().setUserId(userId));
//            IMEventHandler handler = HandlerManager.getHandler(ctx,msg);
//            DisruptorWorker.dispatch(msg.getUserId(), handler);
//            //Worker.dispatch(msg.getUserId(), handler);
//        }

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
            logger.info(">>>netty userEventTriggered事件: 当前channelGroup:"+ConnectionManager.channelGroup.size());

            //ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
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
        ctx.fireChannelRead(msg);

        /**
        Msg userMsg = (Msg)msg;
        //登录事件
        if(userMsg.getEvent() instanceof LoginEvent){
            logger.debug(">>>用户登录事件:"+userMsg.toString());
            //添加到连接管理容器,并设置userId属性到Channel
            ConnectionManager.addConnection(userMsg.getUser().getUserId(),ctx);
            //userId 和host主机映射关系 添加到redis
            ChatRoomRedisManager.setUserIdHostRelation(userMsg.getUser().getUserId());
        }else if(userMsg.getEvent() instanceof LogoutEvent){
            logger.debug(">>>用户登出事件:"+userMsg.toString());
            ConnectionManager.closeConnection(ctx);
            ChatRoomRedisManager.removeUserIdHostRelation(userMsg.getUser().getUserId());
        } else {
            ctx.fireChannelRead(userMsg);
        }
        **/
    }




}
