package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

//登录事件
public class LoginEvent extends BaseEvent{
    public String sessionId;


    public String getSessionId() {
        return sessionId;
    }

    public LoginEvent setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public int getEventType() {
        return EventType.LOGIN.getValue();
    }


}
