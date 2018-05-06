package com.youhaoxi.livelink.gateway.starter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class GatewayServer {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel;
    private static int port = 8080;

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Please give port as argument");
//            System.exit(1);
//        }
//        int port = Integer.parseInt(args[0]);

        final GatewayServer endpoint = new GatewayServer();
        ChannelFuture future = endpoint.startup(port);
        //jvm关闭钩子线程
        Runtime.getRuntime().addShutdownHook(new Thread(()-> endpoint.destroy()));
        future.channel().closeFuture().syncUninterruptibly();
    }


    public ChannelFuture startup(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer());

        ChannelFuture future = bootstrap
                .bind(new InetSocketAddress(port)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    //init Registry
                    logger.info("[GatewayServer] Started Successed, registry is complete, waiting for client connect...");
                } else {
                    logger.error("[GatewayServer] Started Failed, registry is incomplete");
                }
            }
        });

        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    protected ChannelInitializer<Channel> createInitializer() {
        return new ChatServerInitializer();
        //return new DemoInitializer();
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    protected static void bindConnectionOptions(ServerBootstrap bootstrap) {

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_LINGER, 0);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true); //调试用
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //心跳机制暂时使用TCP选项，之后再自己实现

    }


}
