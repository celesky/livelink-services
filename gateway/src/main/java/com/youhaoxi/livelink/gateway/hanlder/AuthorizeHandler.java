package com.youhaoxi.livelink.gateway.hanlder;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.bean.ResultBean;
import com.youhaoxi.livelink.gateway.bean.enums.EventType;
import com.youhaoxi.livelink.gateway.bean.event.*;
import com.youhaoxi.livelink.gateway.bean.msg.Header;
import com.youhaoxi.livelink.gateway.bean.msg.User;
import com.youhaoxi.livelink.gateway.bean.msg.UserMsg;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UserMsg userMsg = (UserMsg)msg;

        //ConnectionManager.channelGroup.writeAndFlush(bean.retain());
        logger.debug("userMsg = " + userMsg.toString());
        //参数检查
        ResultBean resultBean = paramCheck(userMsg);
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
        if(userMsg.getEvent() instanceof LoginEvent) {
            boolean result = authorize(userMsg,ctx);
            //认证通过,传递给下一个ConnectionHandler
            if(result){
                ctx.fireChannelRead(msg);
            }
        }else{
            //session检查
            boolean result = checkSession(ctx);
            //认证通过,传递给下一个ConnectionHandler
            if(result){
                ctx.fireChannelRead(msg);
            }
        }

    }

    private ResultBean paramCheck(UserMsg userMsg) {
        try{
            if(userMsg.getHeader()==null){
                return new ResultBean(100,"header partition not exist");
            }
            if(userMsg.getUser()==null){
                return new ResultBean(100,"user partition not exist");
            }
            if(userMsg.getEventMap()==null){
                return new ResultBean(100,"eventMap partition not exist");
            }
            Header header = userMsg.getHeader();
            User user = userMsg.getUser();
            HashMap eventMap = userMsg.getEventMap();

            //登录消息 map转成对应实体
            MsgEvent msgEvent=null;
            String evenjson = JSON.toJSONString(eventMap);

            if(header.getEventType().getValue()== EventType.LOGIN.getValue()){//登录
                msgEvent = JSON.parseObject(evenjson,LoginEvent.class);
            }else if(header.getEventType().getValue()== EventType.JOINROOM.getValue()){//加入聊天室
                msgEvent = JSON.parseObject(evenjson,JoinRoomEvent.class);
            }else if(header.getEventType().getValue()== EventType.PLAINMSG.getValue()){//普通文本消息
                msgEvent = JSON.parseObject(evenjson,PlainMsgEvent.class);
            }else if(header.getEventType().getValue()== EventType.RICHMSG.getValue()){//道具礼物消息
                msgEvent = JSON.parseObject(evenjson,RichMsgEvent.class);
            }else if(header.getEventType().getValue()== EventType.QUITROOM.getValue()){//退出聊天室
                msgEvent = JSON.parseObject(evenjson,QuitRoomEvent.class);
            }else{
                return new ResultBean(101,"错误eventType");
            }
            userMsg.setEvent(msgEvent);
            userMsg.setEventMap(null);//map置空  释放内存
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

    /**
     * 对用户登录请求进行认证
     * @param userMsg
     * @param ctx
     */
    private boolean authorize(UserMsg userMsg, ChannelHandlerContext ctx) {
        boolean valid = checkLogin(userMsg);
        //登录成功
        if(valid){
            ctx.writeAndFlush(new TextWebSocketFrame("welcome! 登录成功 "));
            return valid;
        }else{
            //非法连接请求
            //关闭连接
            ctx.writeAndFlush(new TextWebSocketFrame("connect refuse!!! 登录失败! ")).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                    ConnectionManager.closeConnection(ctx);
                }
            });
        }
        return false;
    }

    /**
     * todo 调用rpc接口 检查用户权限
     * @param userMsg
     * @return
     */
    private boolean checkLogin(UserMsg userMsg) {
        if(userMsg.user.getUserId()==null){
            return false;
        }
        return true;
    }
}
