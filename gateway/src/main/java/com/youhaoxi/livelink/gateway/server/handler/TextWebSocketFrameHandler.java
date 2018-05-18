package com.youhaoxi.livelink.gateway.server.handler;

import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.server.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 收到消息事件
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             TextWebSocketFrame msg) throws Exception {


        TextWebSocketFrame newmsg = new TextWebSocketFrame("fuck you");
        ConnectionManager.channelGroup.writeAndFlush(newmsg);

        //todo 根据消息类型 登录命令 还是普通消息
        //todo 鉴权
        //todo 添加到自定义连接管理容器
//        Msg userMsg = JSONObject.parseObject(im.text(), Msg.class);
//        System.out.println("userMsg = " + userMsg.toString());
//        if(1==userMsg.getMsgType()){
//            //鉴权
//            authorize(userMsg,ctx);
//        }else if(2==userMsg.getMsgType()){
//            //普通文本消息
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

            ConnectionManager.channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            //添加到连接组
            ConnectionManager.channelGroup.add(ctx.channel());

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 认证授权
     * @param msg
     * @param ctx
     */
    private void authorize(Msg msg, ChannelHandlerContext ctx) {

    }

}
