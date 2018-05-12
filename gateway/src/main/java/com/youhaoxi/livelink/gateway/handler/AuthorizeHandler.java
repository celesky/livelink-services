package com.youhaoxi.livelink.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.im.ResultBean;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.event.*;
import com.youhaoxi.livelink.gateway.im.handler.HandlerManager;
import com.youhaoxi.livelink.gateway.im.handler.IMEventHandler;
import com.youhaoxi.livelink.gateway.im.msg.Header;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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
        Msg msg = (Msg)m;

        //ConnectionManager.channelGroup.writeAndFlush(im.retain());
        logger.debug("userMsg = " + msg.toString());
        //参数检查
        ResultBean resultBean = paramCheck(msg);
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
        if(msg.event instanceof LoginEvent) {
            IMEventHandler handler = HandlerManager.getHandler(ctx,msg);
            Worker.dispatch(msg.user.userId, handler);
        }else{
            //session检查
            boolean result = checkSession(ctx);
            //认证通过,传递给下一个ConnectionHandler
            if(result){
                ctx.fireChannelRead(msg);
            }
        }

    }

    private ResultBean paramCheck(Msg msg) {
        try{
            if(msg.getHeader()==null){
                return new ResultBean(100,"header partition not exist");
            }
            if(msg.getUser()==null){
                return new ResultBean(100,"user partition not exist");
            }
            if(msg.getEventMap()==null){
                return new ResultBean(100,"eventMap partition not exist");
            }
            Header header = msg.getHeader();
            User user = msg.getUser();
            HashMap eventMap = msg.getEventMap();

            //登录消息 map转成对应实体
            IMsgEvent msgEvent=null;
            String evenjson = JSON.toJSONString(eventMap);
            msgEvent = EventJsonParserManager.parseJsonToEvent(header.eventType.getValue(),evenjson);

//            if(header.getEventType().getValue()== EventType.LOGIN.getValue()){//登录
//                msgEvent = JSON.parseObject(evenjson,LoginEvent.class);
//            }else if(header.getEventType().getValue()== EventType.JOINROOM.getValue()){//加入聊天室
//                msgEvent = JSON.parseObject(evenjson,JoinRoomEvent.class);
//            }else if(header.getEventType().getValue()== EventType.PLAINMSG.getValue()){//普通文本消息
//                msgEvent = JSON.parseObject(evenjson,PlainUserMsgEvent.class);
//            }else if(header.getEventType().getValue()== EventType.RICHMSG.getValue()){//道具礼物消息
//                msgEvent = JSON.parseObject(evenjson,RichUserMsgEvent.class);
//            }else if(header.getEventType().getValue()== EventType.QUITROOM.getValue()){//退出聊天室
//                msgEvent = JSON.parseObject(evenjson,QuitRoomEvent.class);
//            }else if(header.getEventType().getValue()== EventType.LOGOUT.getValue()) {//登出
//                msgEvent = JSON.parseObject(evenjson,LogoutEvent.class);
//            }else{
//                return new ResultBean(101,"错误eventType");
//            }
            msg.setEvent(msgEvent);
            msg.setEventMap(null);//map置空  释放内存
            return new ResultBean(0,"");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("paramCheck error!"+e.getMessage(),e);
            return new ResultBean(-1,"发生错误");
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
