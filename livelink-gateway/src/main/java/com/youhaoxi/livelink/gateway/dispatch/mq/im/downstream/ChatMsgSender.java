package com.youhaoxi.livelink.gateway.dispatch.mq.im.downstream;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.common.util.ClientPushUtil;
import com.youhaoxi.livelink.gateway.dispatch.mq.Processor;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class ChatMsgSender implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ChatMsgSender.class);

    @Override
    public void process(byte[] body) {
        try {

            String message = new String(body, "UTF-8");
            logger.info("EndpointSender 下发消息:"+message);
            ResultMsg rmsg = JSON.parseObject(message,ResultMsg.class);
            //下发给dest用户
            ClientPushUtil.writeToClient(rmsg);

        } catch (UnsupportedEncodingException e) {
            logger.error("EndpointSender 出现UnsupportedEncodingException异常:{}",e);
        } catch (Exception e){
            logger.error("EndpointSender 出现Exception异常:{}",e);
        }

    }


    public static void main(String[] args) {
        String s = " {\"code\":10,\"dest\":{\"userId\":111},\"from\":{\"img\":\"asdfas\",\"name\":\"xdsfa\",\"userId\":222},\"msg\":\"hello myname is zhoujielun\"}\n";

        ResultMsg rmsg = JSON.parseObject(s,ResultMsg.class);

        System.out.println(rmsg);
    }

}
