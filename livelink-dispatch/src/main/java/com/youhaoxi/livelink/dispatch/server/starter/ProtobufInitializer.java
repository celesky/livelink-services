package com.youhaoxi.livelink.dispatch.server.starter;

import com.google.protobuf.MessageLite;
import com.youhaoxi.livelink.dispatch.server.handler.GateConnectionHandler;
import com.youhaoxi.livelink.protocol.protobuf.demo.AddressBookProtos;
import com.youhaoxi.livelink.protocol.protobuf.demo.DataInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtobufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite = DataInfo.Datas.getDefaultInstance();


    public ProtobufInitializer() {

    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufDecoder(lite));
        pipeline.addLast(new GateConnectionHandler());

    }
}
