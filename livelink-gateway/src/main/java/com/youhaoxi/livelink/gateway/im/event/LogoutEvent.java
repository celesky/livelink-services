package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

public class LogoutEvent extends BaseEvent{
    @Override
    public int getEventType() {
        return EventType.LOGOUT.getValue();
    }


}
