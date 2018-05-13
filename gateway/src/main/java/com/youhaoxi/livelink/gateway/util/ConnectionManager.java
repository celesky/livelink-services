package com.youhaoxi.livelink.gateway.util;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    //所有的连接 channel组,并会自动管理生命周期,移除断开的连接
    //所有握手成功的连接都暂时会放在里面
    public final static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);


    //保存一个gateway上所有的客户端连接  userId-->ChannelHandlerContext
    private static ConcurrentHashMap<Integer, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();
    public static AttributeKey<Integer> AK_USER_ID = AttributeKey.valueOf("userId");


    private static void addToCtxMap(Integer userId,ChannelHandlerContext ctx){
        //给ctx加上userId属性
        ctx.channel().attr(AK_USER_ID).set(userId);
        ctxMap.put(userId,ctx);
    }

    private static void removeFrmCtxMap(Integer userId,ChannelHandlerContext ctx){
        ctxMap.remove(userId);
    }

    private static void removeFrmCtxMap(ChannelHandlerContext ctx){
        Integer userId = ctx.channel().attr(AK_USER_ID).get();
        if(userId!=null){
            ctxMap.remove(userId);
        }

    }

    /**
     * 新增  统一方法
     * 清理资源
     */
    public static void addConnection(Integer userId,ChannelHandlerContext ctx){
        //给ctx绑定userId属性 每个合法的用户连接都有userId属性
        ctx.channel().attr(AK_USER_ID).set(userId);
        if(ctxMap.contains(userId)){
            //如果原来有ctx对象,关闭之
            ChannelHandlerContext _ctx  = ctxMap.remove(userId);
            if(_ctx.channel().isOpen()||_ctx.channel().isActive()){
                _ctx.close();
            }
        }
        ctxMap.put(userId,ctx);
    }


    /**
     * 断开连接统一方法
     * 断开后从ctxMap中remove
     */
    public static void closeConnection(ChannelHandlerContext ctx){
        //如果连接还存活,先关闭之
        if(ctx.channel().isOpen()||ctx.channel().isActive()){
            ctx.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    removeFrmCtxMap(ctx);
                }
            });

        }else{
            removeFrmCtxMap(ctx);
        }
    }


    /**
     * 检查连接是否是授权连接
     * @param ctx
     * @return
     */
    public static boolean checkConnectionAuth(ChannelHandlerContext ctx){
        Integer userId = ctx.channel().attr(AK_USER_ID).get();
        if(userId!=null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 把用户id和host 注册到redis
     */
    public static void registerHostToRedis(Integer userId){

    }

    public static Integer getUserIdInCtx(ChannelHandlerContext ctx){
        Integer userId = ctx.channel().attr(AK_USER_ID).get();
        return userId;
    }

    public static ConcurrentHashMap<Integer, ChannelHandlerContext> getCtxMap() {
        return ctxMap;
    }


}

