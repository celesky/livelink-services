package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

public class LogoutEvent implements IMsgEvent {
    @Override
    public int getEventType() {
        return EventType.LOGOUT.getValue();
    }
}
