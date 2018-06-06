package com.youhaoxi.livelink.gateway.dispatch.client;

import com.youhaoxi.livelink.gateway.dispatch.client.handler.DispatchConnectionHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 派发服务连接管理
 */
public class DispatchConnManager {
    private static final Logger logger = LoggerFactory.getLogger(DispatchConnManager.class);

    public final static ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    //dispatch服务器的所有连接
    private static List<ChannelHandlerContext> dispConnList = new ArrayList();

    /**
     * 获取所有连接
     * @return
     */
    public static Collection<ChannelHandlerContext> getAllConn(){
        return dispConnList;
    }

    /**
     * 添加一个连接到容器
     */
    public static void addConn(ChannelHandlerContext ctx){
        dispConnList.add(ctx);
        //如果之前的连接还活着 关闭它
//        if(old!=null&&(old.channel().isActive()||old.channel().isOpen())){
//            try{
//                old.close();
//            }catch (Exception e){
//                logger.error("addConn...旧连接关闭出现错误...");
//            }
//
//        }
    }
    /**
     * 从容器移除一个连接
     */
    public static void removeConn(ChannelHandlerContext ctx){
        if(ctx!=null&&(ctx.channel().isActive()||ctx.channel().isOpen())){
            try{
                ctx.close();
            }catch (Exception e){
                logger.error("removeConn...关闭连接出现错误...");
            }
        }
        dispConnList.remove(ctx);
    }

    /**
     * 获取一个连接
     * @return
     */
    public static ChannelHandlerContext getOneConnRamdom(){
        ChannelHandlerContext ctx =  dispConnList.get(0);
        if(ctx!=null&&ctx.channel().isOpen()&&ctx.channel().isActive()){
            return ctx;
        }else{
            //todo  重新获取一个
            return ctx;
        }
    }

}
