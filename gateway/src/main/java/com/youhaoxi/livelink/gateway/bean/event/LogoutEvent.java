package com.youhaoxi.livelink.gateway.bean.event;

import com.youhaoxi.livelink.gateway.bean.enums.EventType;

public class LogoutEvent implements MsgEvent{
    @Override
    public int getEventType() {
        return EventType.LOGOUT.getValue();
    }
}
