package com.youhaoxi.livelink.gateway.dispatch.client.starter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 连接dispatch服务
 */
public class DispachServerClient {

    private static final Logger logger = LoggerFactory.getLogger(DispachServerClient.class);


    public void connect(String ip,int port){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(createProtoBufInitializer());

        ChannelFuture future = bootstrap.connect(
                                new InetSocketAddress(ip, port));

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    logger.info("Connection established");
                } else {
                    logger.info("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });

    }

    public ProtobufInitializer createProtoBufInitializer(){
        return new ProtobufInitializer();
    }

    public static void main(String[] args) {
        new DispachServerClient().connect("127.0.0.1",7070);

    }
}
