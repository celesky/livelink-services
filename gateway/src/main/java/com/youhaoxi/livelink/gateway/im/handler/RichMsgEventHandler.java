package com.youhaoxi.livelink.gateway.im.handler;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.MqEventDispatcher;
import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.enums.RichMsgType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.event.RichUserMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;
import com.youhaoxi.livelink.gateway.im.msg.User;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RichMsgEventHandler extends IMEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(RichMsgEventHandler.class);

    private MqEventDispatcher dispatcher;

    public RichMsgEventHandler(ChannelHandlerContext ctx, IMsgEvent msg) {
        super(ctx, msg);
    }

    @Override
    public void execute(Worker woker) {
        RichUserMsgEvent richMsgEvent = (RichUserMsgEvent)msg;

        //组织下发消息对象
        ResultMsg rmsg ;
        //礼物类型
        if(richMsgEvent.richMsgType.getValue()== RichMsgType.GIFT.getValue()){
            rmsg = new ResultMsg(30,"礼物消息");
        }else if(richMsgEvent.richMsgType.getValue()== RichMsgType.PROP.getValue()){
            //道具类型
            rmsg = new ResultMsg(20,"道具消息");
        }else{
            logger.error("不能识别的RichMsgType:{},richMsgEvent:{}",richMsgEvent.richMsgType,richMsgEvent.toString());
            return ;
        }
        //发送人
        rmsg.setFrom(richMsgEvent.from);

        if(richMsgEvent.getBroadType().getValue() == BroadType.P2P.getValue()){
            rmsg.setDest(new User()
                    .setUserId(richMsgEvent.getReceiverUserId()));
            woker.dispatcher.dispatch(rmsg);
        }else if( richMsgEvent.getBroadType().getValue() == BroadType.MASS.getValue()){
            //群发
            woker.dispatcher.groupDispatch(rmsg,richMsgEvent.receiveRoomId);
        }else{
            logger.error("不能识别的broadType:{},richMsgEvent:{}",richMsgEvent.getBroadType(),richMsgEvent.toString());
            return ;
        }
    }
}
