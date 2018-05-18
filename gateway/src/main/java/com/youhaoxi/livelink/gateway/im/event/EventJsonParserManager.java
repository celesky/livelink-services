package com.youhaoxi.livelink.gateway.im.event;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.enums.RichMsgType;
import com.youhaoxi.livelink.gateway.im.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件json转换器管理
 */
public class EventJsonParserManager {
    private static final Logger logger = LoggerFactory.getLogger(EventJsonParserManager.class);

    /**
     * 用户事件typeId-->用户事件实体class
     */
    public static ConcurrentHashMap<Integer, Class<? extends IMsgEvent>> eventMap = new ConcurrentHashMap<>();


    public static void registerEvent(int eventType, Class<? extends IMsgEvent> clazz) {
        eventMap.put(eventType,clazz);
    }

    public static <T extends IMsgEvent> T parseJsonToEvent(int eventType,String eventJson){
        Class<T> clazz =  (Class<T>)eventMap.get(eventType);
        if(clazz==null){
            logger.error("EventJsonParserManager getEventJsonParser  eventType:{} 未注册的事件类型",eventType);
            return null;
        }
        T msgEvent =  (T)JSON.parseObject(eventJson,clazz);
        return msgEvent;
    }



    public static void initParsers() {
        eventMap.put(EventType.TEST.getValue(),TestEvent.class);
        eventMap.put(EventType.CREATEROOM.getValue(),CreateRoomEvent.class);
        eventMap.put(EventType.LOGIN.getValue(),LoginEvent.class);
        eventMap.put(EventType.LOGOUT.getValue(),LogoutEvent.class);
        eventMap.put(EventType.JOINROOM.getValue(),JoinRoomEvent.class);
        eventMap.put(EventType.QUITROOM.getValue(),QuitRoomEvent.class);
        eventMap.put(EventType.PLAINMSG.getValue(),PlainUserMsgEvent.class);
        eventMap.put(EventType.RICHMSG.getValue(),RichUserMsgEvent.class);
        eventMap.put(EventType.STATS.getValue(),StatsEvent.class);

    }
}
