package com.youhaoxi.livelink.gateway.server.handler;

import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

//    private static final ByteBuf HEARTBEAT_SEQUENCE =
//            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer( "HEARTBEAT", CharsetUtil.ISO_8859_1));

    private static  TextWebSocketFrame HEART_BEAT = new TextWebSocketFrame("DING");

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            HEART_BEAT = new TextWebSocketFrame("DING");
            //先发条消息下去确认下 如果发送
            ctx.writeAndFlush(HEART_BEAT).addListener(
                    new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) {
                            if (!future.isSuccess()) {
                                future.channel().close();
                                // 从自定义容器中删除连接对象
                                ConnectionManager.closeConnection(ctx);
                            }
                        }
                    });
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 移除断开的连接
     */
    public void removeBrockenConn(ChannelFuture future){

    }

}
