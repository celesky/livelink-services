package com.youhaoxi.livelink.gateway.server.decoder;

import com.youhaoxi.livelink.gateway.im.JsonEventPackUtil;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 文本消息转消息实体
 * json转实体
 * 暂定
 * eventype|json
 */
public class TextWSFrameToMsgDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(TextWSFrameToMsgDecoder.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        logger.debug(msg.text());
        IMsgEvent msgEvent = JsonEventPackUtil.unPack(msg.text());
        out.add(msgEvent);
    }

    public static void main(String[] args) {
        String txt = "1dasfsadf";
        char ctype = txt.charAt(0);
        String type = String.valueOf( txt.charAt(0));
        String json = txt.substring(1,txt.length());
        System.out.println(json);
    }


}
