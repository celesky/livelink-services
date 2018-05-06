package com.youhaoxi.livelink.gateway.bean.event;

import com.youhaoxi.livelink.gateway.bean.enums.EventType;

//退出聊天室
public class QuitRoomEvent implements MsgEvent{
    private Integer userId;
    private String roomId;

    @Override
    public int getEventType() {
        return EventType.QUITROOM.getValue();
    }

    public Integer getUserId() {
        return userId;
    }

    public QuitRoomEvent setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public QuitRoomEvent setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    @Override
    public String toString() {
        return "QuitRoomEvent{" +
                "userId=" + userId +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
