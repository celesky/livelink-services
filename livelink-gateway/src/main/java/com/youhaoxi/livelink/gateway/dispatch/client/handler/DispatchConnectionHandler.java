package com.youhaoxi.livelink.gateway.dispatch.client.handler;

import com.google.protobuf.MessageLite;
import com.youhaoxi.livelink.gateway.dispatch.client.DispatchConnManager;
import com.youhaoxi.livelink.protocol.protobuf.demo.AddressBookProtos;
import com.youhaoxi.livelink.protocol.protobuf.demo.DataInfo;
import com.youhaoxi.livelink.protocol.protobuf.demo.HelloWorldProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatchConnectionHandler  extends SimpleChannelInboundHandler<MessageLite> {
    private static final Logger logger = LoggerFactory.getLogger(DispatchConnectionHandler.class);

    /**
     * 与dispatch服务器建立连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("已连接上dispach服务端..........发送一个狗过去");
        DispatchConnManager.addConn(ctx);
        //发送一条登录消息过去,包含自己的host和端口地址
//        DataInfo.Datas data = DataInfo.Datas.newBuilder()
//                .setDataType(DataInfo.Datas.DataType.dogType)
//                .setDog(
//                        DataInfo.Dog.newBuilder()
//                                .setAge(23)
//                                .setColor("红色")
//                                .setHeight(3.5f)
//                ).build();
//        ctx.writeAndFlush(data);
        //ctx.writeAndFlush(john);
        //logger.info("发送完成........");
    }


    /**
     * 来自dispach服务器推送过来的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageLite msg) throws Exception {
        //AddressBookProtos.Person person = (AddressBookProtos.Person)msg;
        DataInfo.Person person = ((DataInfo.Datas)msg).getPerson();
        logger.info("收到来自dispatch服务的消息........"+person.toString());

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DispatchConnManager.removeConn(ctx);
    }
}
