package com.youhaoxi.livelink.gateway.server.starter;

import com.youhaoxi.livelink.gateway.cache.ChatRoomRedisManager;
import com.youhaoxi.livelink.gateway.common.ConfigPropertes;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConnectionManager;
import com.youhaoxi.livelink.gateway.dispatch.mq.downstream.EndpointSender;
import com.youhaoxi.livelink.gateway.dispatch.mq.downstream.ReceiverTask;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.MqRstMsgDispatcher;
import com.youhaoxi.livelink.gateway.im.event.EventJsonParserManager;
import com.youhaoxi.livelink.gateway.im.handler.HandlerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class GatewayServer {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServer.class);
    private final ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel;

    @Autowired
    private ConfigPropertes configPropertes;



    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Please give port as argument");
//            System.exit(1);
//        }
//        int port = Integer.parseInt(args[0]);


    }

    /**
     * 读取配置文件中配置的serverchannel类型
     * @return
     */
    private Class serverSocketChannelClazz(){
        String channelClazz = configPropertes.serverSocketChannel;
        try {
            Class clazz = Class.forName(channelClazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            logger.error("serverSocketChannelClazz 异常:{}",e);
        }
        //返回默认
        return NioServerSocketChannel.class;
    }

    public void startup(){
        //启动工作线程组
        Worker.startWorker(configPropertes.WORK_NUM, MqRstMsgDispatcher.class, LinkedBlockingDeque.class);
        //启动netty
        //final GatewayServer endpoint = new GatewayServer();
        ChannelFuture future = this.boot(configPropertes.SERVER_PORT);

        //注册信号监听事件 kill -15
        KillHandler killHandler = new KillHandler();
        killHandler.registerSignal("TERM");
        //jvm关闭钩子线程 做退出资源清理
        Runtime.getRuntime().addShutdownHook(new Thread(()-> this.destroy()));

        future.channel().closeFuture().syncUninterruptibly();
    }



    public ChannelFuture boot(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(serverSocketChannelClazz())
                .childHandler(createInitializer());

        ChannelFuture future = bootstrap
                .bind(new InetSocketAddress(port)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    //init Registry
                    HandlerManager.initHandlers();
                    EventJsonParserManager.initParsers();
                    //启动下发任务
                    singleExecutor.submit(new ReceiverTask(new EndpointSender()));
                    ;
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
        logger.info(">>>准备开始JVM退出前清理工作...");
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();

        //mq连接关闭
        RabbitConnectionManager.getInstance().closeConnection();
        //连接在本机的所有用户
        ChatRoomRedisManager.clearAllRelationThisHost();
        //下发任务关闭
        singleExecutor.shutdown();

        logger.info(">>>清理工作完成,虚拟机退出...");

    }

    protected void bindConnectionOptions(ServerBootstrap bootstrap) {

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_LINGER, 0);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true); //调试用
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //心跳机制暂时使用TCP选项，之后再自己实现

    }


}
