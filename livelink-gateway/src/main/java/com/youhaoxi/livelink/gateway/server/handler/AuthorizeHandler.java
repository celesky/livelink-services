package com.youhaoxi.livelink.gateway.server.handler;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.dispatch.work.Worker;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.event.*;
import com.youhaoxi.livelink.gateway.im.handler.HandlerManager;
import com.youhaoxi.livelink.gateway.im.handler.IMEventHandler;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过用户发上来的消息进行
 * 连接认证管理
 */
public class AuthorizeHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeHandler.class);

    /**
     * 收到消息事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object m) throws Exception {
        IMsgEvent msg = (IMsgEvent)m;

        //ConnectionManager.channelGroup.writeAndFlush(im.retain());
        logger.debug("userMsg = " + msg.toString());
        //参数检查
        ResultMsg resultBean = paramCheck(msg);
        if(resultBean.getCode()!=0) {
            ReferenceCountUtil.release(msg);
            String resultJson = JSON.toJSONString(resultBean);
            //参数检查异常
            ctx.writeAndFlush(new TextWebSocketFrame(resultJson)).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                    ConnectionManager.closeConnection(ctx);
                }
            });
        }
        //登录事件  鉴权
        if(msg.getEventType()== EventType.LOGIN.getValue()) {
            IMEventHandler handler = HandlerManager.getHandler(ctx,msg);
            Worker.dispatch(msg.getUserId(), handler);
        }else{
            //session检查
            boolean result = checkSession(ctx);
            //认证通过,传递给下一个ConnectionHandler
            if(result){
                ctx.fireChannelRead(msg);
            }
        }

    }

    private ResultMsg paramCheck(IMsgEvent msg) {
        try{
            BaseEvent baseEvent = (BaseEvent)msg;
            if(baseEvent.getHeader()==null){
                return new ResultMsg(100,"header partition not exist");
            }
            if(baseEvent.getFrom()==null){
                return new ResultMsg(100,"user partition not exist");
            }

            return new ResultMsg(0,"");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("paramCheck error!"+e.getMessage(),e);
            return new ResultMsg(-1,"发生错误");
        }

    }

    /**
     * 检查用户连接认证状态
     * @param ctx
     */
    private boolean checkSession(ChannelHandlerContext ctx) {
        return ConnectionManager.checkConnectionAuth(ctx);
    }


}
