package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

public class CreateRoomEvent extends BaseEvent {

    @Override
    public int getEventType() {
        return EventType.CREATEROOM.getValue();
    }


}
