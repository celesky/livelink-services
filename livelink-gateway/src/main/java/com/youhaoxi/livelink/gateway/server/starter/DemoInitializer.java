package com.youhaoxi.livelink.gateway.server.starter;

import com.youhaoxi.livelink.gateway.server.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class DemoInitializer extends ChannelInitializer<Channel> {
//    private final ChannelGroup channelGroup;
//
//    public ChatServerInitializer(ChannelGroup channelGroup) {
//        this.channelGroup = channelGroup;
//    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        //pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        //暂不需要响应http请求
        //pipeline.addLast(new HttpRequestHandler("/ws"));
        //按照 WebSocket 规范的要求，处理 WebSocket 升级握手、PingWebSocketFrame 、 PongWebSocketFrame 和 CloseWebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new TextWebSocketFrameHandler());


    }
}
