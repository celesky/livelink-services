package com.youhaoxi.livelink.gateway.im.msg;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

import java.time.LocalDateTime;

public class Header{
    //事件类型 1:登录. 2:加入聊天室 3:发送文字消息 4:发送道具\礼物消息 5:退出聊天室
    public EventType eventType;
    public String sessionId;
    public String uuid;
    public LocalDateTime dateTime;//消息时间

    public EventType getEventType() {
        return eventType;
    }

    public Header setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }



    public String getSessionId() {
        return sessionId;
    }

    public Header setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Header setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Header setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    @Override
    public String toString() {
        return "Header{" +
                "eventType=" + eventType +
                ", sessionId='" + sessionId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
