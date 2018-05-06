package com.youhaoxi.livelink.gateway.decoder;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.bean.msg.UserMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * json转实体
 */
public class TextWSFrameToMsgDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(TextWSFrameToMsgDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        logger.debug(msg.text());
        UserMsg userMsg = JSONObject.parseObject(msg.text(), UserMsg.class);
        out.add(userMsg);
    }
}
