package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

//登录事件
public class LoginEvent implements IMsgEvent {
    public Integer userId;
    public String sessionId;

    public Integer getUserId() {
        return userId;
    }

    public LoginEvent setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

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

    @Override
    public String toString() {
        return "LoginEvent{" +
                "userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
