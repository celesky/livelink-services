package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

//加入聊天室
public class JoinRoomEvent implements IMsgEvent {
    public Integer userId;
    public String roomId;

    @Override
    public int getEventType() {
        return EventType.JOINROOM.getValue();
    }

    public Integer getUserId() {
        return userId;
    }

    public JoinRoomEvent setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public JoinRoomEvent setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    @Override
    public String toString() {
        return "JoinRoomEvent{" +
                "userId=" + userId +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
