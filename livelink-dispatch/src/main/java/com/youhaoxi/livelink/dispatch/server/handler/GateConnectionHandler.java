package com.youhaoxi.livelink.dispatch.server.handler;

import com.google.protobuf.MessageLite;
import com.youhaoxi.livelink.dispatch.server.GateConnManager;
import com.youhaoxi.livelink.protocol.protobuf.demo.AddressBookProtos;
import com.youhaoxi.livelink.protocol.protobuf.demo.DataInfo;
import com.youhaoxi.livelink.protocol.protobuf.demo.HelloWorldProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 聊天消息处理
 */
public class GateConnectionHandler extends SimpleChannelInboundHandler<MessageLite> {
    private static final Logger logger = LoggerFactory.getLogger(GateConnectionHandler.class);


    /**
     *  first
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有gateway客户端连接上来了,发个提示信息过去");

//        DataInfo.Datas data = DataInfo.Datas.newBuilder()
//                .setDataType(DataInfo.Datas.DataType.personType)
//                .setPerson(
//                        DataInfo.Person.newBuilder()
//                                .setId(23)
//                                .setGender(DataInfo.Person.Gender.female)
//                                .setName("zhangsan")
//                )
//                .build();
//
        //ctx.writeAndFlush(john);
        GateConnManager.channelGroup.add(ctx.channel());


    }


    /**
     * JOINROOM(2), PLAINMSG(3), RICHMSG( 4),QUITROOM(5),LOGOUT(6)
     * @param ctx
     * @param lite
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageLite lite) throws Exception {

        //gateway注册消息 gateway regist消息 {host}
        String gateHost="";
        GateConnManager.addConn(gateHost,ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("报错了!!!!!!!!!!!!!!{}",cause);
        //ConnectionManager.closeConnection(ctx);
    }


}
