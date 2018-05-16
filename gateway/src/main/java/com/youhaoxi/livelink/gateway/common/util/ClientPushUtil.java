package com.youhaoxi.livelink.gateway.common.util;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientPushUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClientPushUtil.class);


    public static void writeToClient(ResultMsg rmsg){
        Integer userId = rmsg.getDest().getUserId();
        if(userId==null){
            logger.info("writeToClient userId:{}为空,msg:{}",userId,rmsg);
        }
        String json = JSON.toJSONString(rmsg);
        ChannelHandlerContext ctx = ConnectionManager.getCtxMap().get(userId);
        if(ctx==null){
            logger.info("writeToClient userId:{}找不到可用连接ctx,msg:{}",userId,json);
        }

        if(ctx.channel().isActive()&&ctx.channel().isOpen()){
            ctx.writeAndFlush(new TextWebSocketFrame(json));
        }else{
            logger.info("writeToClient userId:{} ctx连接已断开,msg:{}",userId,json);
        }
    }




    public static void writeToClient(Integer userId,String json){
        ChannelHandlerContext ctx = ConnectionManager.getCtxMap().get(userId);
        if(ctx==null){
            logger.info("writeToClient userId:{}找不到可用连接ctx,msg:{}",userId,json);
        }

        if(ctx.channel().isActive()&&ctx.channel().isOpen()){
            ctx.writeAndFlush(new TextWebSocketFrame(json));
        }else{
            logger.info("writeToClient userId:{} ctx连接已断开,msg:{}",userId,json);
        }
    }

    public static void writeToClient(ChannelHandlerContext ctx,String json,Integer userId){
        if(ctx==null){
            logger.info("writeToClient userId:{} ctx为空,msg:{}",userId,json);
        }

        if(ctx.channel().isActive()&&ctx.channel().isOpen()){
            ctx.writeAndFlush(new TextWebSocketFrame(json));
        }else{
            logger.info("writeToClient userId:{} ctx连接已断开,msg:{}",userId,json);
        }
    }

    public static void writeToClient(ChannelHandlerContext ctx, ResultMsg resultBean, Integer userId){
        String json = JSON.toJSONString(resultBean);
        if(ctx==null){
            logger.info("writeToClient userId:{} ctx为空,msg:{}",userId,json);
        }

        if(ctx.channel().isActive()&&ctx.channel().isOpen()){

            ctx.writeAndFlush(new TextWebSocketFrame(json));
        }else{
            logger.info("writeToClient userId:{} ctx连接已断开,msg:{}",userId,json);
        }
    }
}
