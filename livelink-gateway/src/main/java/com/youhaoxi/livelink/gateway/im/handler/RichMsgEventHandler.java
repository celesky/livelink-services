package com.youhaoxi.livelink.gateway.im.handler;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.dispatch.mq.im.upstream.MqEventDispatcher;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.enums.RichMsgType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.RichUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;

public class RichMsgEventHandler extends IMEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(RichMsgEventHandler.class);

    private MqEventDispatcher dispatcher;

    public RichMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(IWorker woker) {
        RichUserMsgEvent msgEvent = (RichUserMsgEvent)msg;

        //组织下发消息对象
        EndpointMsg rmsg ;
        //礼物类型
        if(msgEvent.richMsgType.getValue()== RichMsgType.GIFT.getValue()){
            rmsg = new EndpointMsg(30,"礼物消息");
        }else if(msgEvent.richMsgType.getValue()== RichMsgType.PROP.getValue()){
            //道具类型
            rmsg = new EndpointMsg(20,"道具消息");
        }else{
            logger.error("不能识别的RichMsgType:{},richMsgEvent:{}",msgEvent.richMsgType,msgEvent.toString());
            return ;
        }
        //发送人
        rmsg.setFrom(msgEvent.from);

        if(msgEvent.getBroadType().getValue() == BroadType.P2P.getValue()){
            rmsg.setDest(new User()
                    .setUserId(msgEvent.getReceiverUserId()));
            rmsg.setTimestamp(msgEvent.getHeader().getDateTime().toEpochSecond(ZoneOffset.of("+8")));//设置时间

            woker.getDispatcher().dispatch(rmsg);
        }else if( msgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            //群发
            woker.getDispatcher().groupDispatch(rmsg,msgEvent.receiveRoomId);
        }else{
            logger.error("不能识别的broadType:{},richMsgEvent:{}",msgEvent.getBroadType(),msgEvent.toString());
            return ;
        }
    }
}
