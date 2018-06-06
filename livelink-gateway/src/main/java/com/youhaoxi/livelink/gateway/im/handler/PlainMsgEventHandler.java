package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.dispatch.mq.im.upstream.MqEventDispatcher;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;

public class PlainMsgEventHandler extends IMEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlainMsgEventHandler.class);

    private MqEventDispatcher dispatcher;

    public PlainMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(IWorker woker) {
        logger.debug(">>>用户普通消息事件处理:"+msg.toString());
        PlainUserMsgEvent msgEvent = (PlainUserMsgEvent)msg;

        //组织下发消息对象

        if(msgEvent.getBroadType().getValue() == BroadType.P2P.getValue()){
            EndpointMsg rmsg = new EndpointMsg(10,msgEvent.msgContent);
            rmsg.setFrom(msgEvent.getFrom());
            rmsg.setDest(new User()
                    .setUserId(msgEvent.getReceiverUserId()));
            rmsg.setTimestamp(msgEvent.getHeader().getDateTime().toEpochSecond(ZoneOffset.of("+8")));//设置时间
            woker.getDispatcher().dispatch(rmsg);

        }else if( msgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            EndpointMsg rmsg = new EndpointMsg(20,msgEvent.msgContent);
            rmsg.setFrom(msgEvent.getFrom());
            rmsg.setTimestamp(msgEvent.getHeader().getDateTime().toEpochSecond(ZoneOffset.of("+8")));//设置时间
            //群发
            woker.getDispatcher().groupDispatch(rmsg,msgEvent.receiveRoomId);
        }else{
            logger.error("不能识别的broadType:{},msgEvent:{}",msgEvent.broadType,msgEvent.toString());
            return ;
        }

    }
}
