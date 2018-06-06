package com.youhaoxi.livelink.gateway.dispatch.client.upstream;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.client.DispatchConnManager;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitProducer;
import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InterMsgDispatcher implements com.youhaoxi.livelink.gateway.dispatch.InterMsgDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(InterMsgDispatcher.class);
//    private static final ExecutorService groupDispatchExecutor =
//            new ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    /**
     * todo  使用protobuf重新定义
     * @param msg
     */
    @Override
    public void dispatch(InterMsg msg) {

        ChannelHandlerContext ctx = DispatchConnManager.getOneConnRamdom();
        //发送到dispatch端
        ctx.writeAndFlush(msg);
    }


}
