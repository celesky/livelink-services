package com.youhaoxi.livelink.gateway.im.handler;


import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.IMsg;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by pan
 */
public class HandlerManager {

    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private static final Map<Integer, Constructor<? extends IMEventHandler>> _handlers = new HashMap<>();

    public static void register(int eventType, Class<? extends IMEventHandler> handler) {

        try {
            Constructor<? extends IMEventHandler> constructor = handler.getConstructor(ChannelHandlerContext.class, IMsgEvent.class);
            _handlers.put(eventType, constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static IMEventHandler getHandler(ChannelHandlerContext ctx,IMsgEvent msg)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int eventType=msg.getEventType();
        Constructor<? extends IMEventHandler> constructor = _handlers.get(eventType);
        if(constructor == null) {
            logger.error("IMEventHandler not exist, eventType: {} msg:{}", eventType,msg.toString());
            return null;
        }

        return constructor.newInstance(ctx,msg);
    }

    public static void initHandlers() {
        HandlerManager.register(EventType.TEST.getValue(), TestEventHandler.class);
        HandlerManager.register(EventType.CREATEROOM.getValue(), CreateRoomEventHandler.class);
        HandlerManager.register(EventType.LOGIN.getValue(), LoginEventHandler.class);
        HandlerManager.register(EventType.LOGOUT.getValue(), LogoutEventHandler.class);
        HandlerManager.register(EventType.JOINROOM.getValue(), JoinRoomEventHandler.class);
        HandlerManager.register(EventType.QUITROOM.getValue(), QuitRoomEventHandler.class);
        HandlerManager.register(EventType.PLAINMSG.getValue(), PlainMsgEventHandler.class);
        HandlerManager.register(EventType.RICHMSG.getValue(), RichMsgEventHandler.class);
        HandlerManager.register(EventType.STATS.getValue(), StatsEventHandler.class);
    }
}
