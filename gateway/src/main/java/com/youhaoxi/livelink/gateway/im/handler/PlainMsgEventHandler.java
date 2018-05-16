package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.MqEventDispatcher;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.PlainUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainMsgEventHandler extends IMEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlainMsgEventHandler.class);

    private MqEventDispatcher dispatcher;

    public PlainMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        logger.debug(">>>用户普通消息事件处理:"+msg.toString());
        PlainUserMsgEvent plainMsgEvent = (PlainUserMsgEvent)msg;

        //组织下发消息对象


        if(plainMsgEvent.getBroadType().getValue() == BroadType.P2P.getValue()){
            ResultMsg rmsg = new ResultMsg(10,plainMsgEvent.msgContent);
            rmsg.setFrom(plainMsgEvent.getFrom());
            rmsg.setDest(new User()
                    .setUserId(plainMsgEvent.getReceiverUserId()));

            woker.dispatcher.dispatch(rmsg);

        }else if( plainMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            ResultMsg rmsg = new ResultMsg(20,plainMsgEvent.msgContent);
            rmsg.setFrom(plainMsgEvent.getFrom());
            //群发
            woker.dispatcher.groupDispatch(rmsg,plainMsgEvent.receiveRoomId);
        }else{
            logger.error("不能识别的broadType:{},plainMsgEvent:{}",plainMsgEvent.broadType,plainMsgEvent.toString());
            return ;
        }

    }
}
