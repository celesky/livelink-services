package com.youhaoxi.livelink.dispatch.server.starter;

import com.youhaoxi.livelink.dispatch.server.handler.HeartbeatHandler;
import com.youhaoxi.livelink.dispatch.server.handler.GateConnectionHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
//    private final ChannelGroup channelGroup;
//
//    public ChatServerInitializer(ChannelGroup channelGroup) {
//        this.channelGroup = channelGroup;
//    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        //暂不需要响应http请求
        //pipeline.addLast(new HttpRequestHandler("/ws"));
        //按照 WebSocket 规范的要求，处理 WebSocket 升级握手、PingWebSocketFrame 、 PongWebSocketFrame 和 CloseWebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //pipeline.addLast(new TextWSFrameToMsgDecoder());//websocketFrame转换成消息实体
        //pipeline.addLast(new AuthorizeHandler());// 鉴权管理
        pipeline.addLast(new GateConnectionHandler());//连接管理
        pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));//空闲连接管理
        pipeline.addLast(new HeartbeatHandler());//心跳处理
        pipeline.addLast(new GateConnectionHandler());//消息处理

    }
}
