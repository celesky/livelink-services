package com.youhaoxi.livelink.dispatch.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GateConnManager {
    public final static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private static final Logger logger = LoggerFactory.getLogger(GateConnManager.class);
    //根据destUserId-->destGateHost
    //所有gateway实例列表 destGateHost-->ctx
    private static Map<String ,ChannelHandlerContext> gateConnMap = new ConcurrentHashMap<>();


    /**
     * 添加一个连接到容器
     */
    public static void addConn(String host,ChannelHandlerContext ctx){
        ChannelHandlerContext old = gateConnMap.putIfAbsent(host,ctx);
        //如果之前的连接还活着 关闭它
        if(old!=null&&(old.channel().isActive()||old.channel().isOpen())){
            try{
                old.close();
            }catch (Exception e){
                logger.error("addConn...旧连接关闭出现错误...");
            }
        }
    }
    /**
     * 从容器移除一个连接
     */
    public static void removeConn(String host){
        ChannelHandlerContext ctx = gateConnMap.get(host);
        if(ctx!=null&&(ctx.channel().isActive()||ctx.channel().isOpen())){
            try{
                ctx.close();
            }catch (Exception e){
                logger.error("removeConn...关闭连接出现错误...");
            }
        }
        gateConnMap.remove(host);

    }




}
