package com.youhaoxi.livelink.gateway.bean.event;

import com.youhaoxi.livelink.gateway.bean.enums.EventType;

//加入聊天室
public class JoinRoomEvent implements MsgEvent{
    private Integer userId;
    private String roomId;

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
