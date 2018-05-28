package com.youhaoxi.livelink.gateway.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.dispatch.work.DisruptorWorker;
import com.youhaoxi.livelink.gateway.dispatch.work.Worker;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.handler.HandlerManager;
import com.youhaoxi.livelink.gateway.im.handler.IMEventHandler;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天消息处理
 */
public class MsgHandler extends SimpleChannelInboundHandler<IMsgEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MsgHandler.class);
//    private static final ExecutorService executorService = new ThreadPoolExecutor(8,
//    16,
//    60,
//    TimeUnit.SECONDS,
//    new LinkedBlockingDeque<>(1000));

    /**
     * JOINROOM(2), PLAINMSG(3), RICHMSG( 4),QUITROOM(5),LOGOUT(6)
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMsgEvent msg) throws Exception {
        //ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame(jsonMsg));
        logger.info(">>>server channelRead事件:"+JSONObject.toJSONString(msg)+" \n当前channelGroup:"+ConnectionManager.channelGroup.size());
        //聊天状态处理
        //将消息交给Woker队列去处理
        IMEventHandler handler = HandlerManager.getHandler(ctx,msg);
        DisruptorWorker.dispatch(msg.getUserId(), handler);
        //Worker.dispatch(msg.getUserId(), handler);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("报错了!!!!!!!!!!!!!!{}",cause);
        ConnectionManager.closeConnection(ctx);
    }


}
