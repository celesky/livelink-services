package com.youhaoxi.livelink.gateway.dispatch.mq.downstream;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.im.JsonMsgPackUtil;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.util.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.UnsupportedEncodingException;

public class EndpointSender implements Sender{

    @Override
    public void send(byte[] body) {
        try {
            //
            String message = new String(body, "UTF-8");
            IMsgEvent event = JsonMsgPackUtil.unPack(message);

            Integer userId = event.getUserId();
            String json = JSON.toJSONString(event);
            writeToClient(userId,json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void writeToClient(Integer userId,String json){
        ChannelHandlerContext ctx = ConnectionManager.getCtxMap().get(userId);
        ctx.writeAndFlush(new TextWebSocketFrame(json));
    }
}
