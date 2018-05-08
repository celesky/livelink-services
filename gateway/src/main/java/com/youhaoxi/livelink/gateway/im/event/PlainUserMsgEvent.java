package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.enums.EventType;

public class PlainUserMsgEvent extends UserMsgEvent {

    public String msgContent;

    @Override
    public int getEventType() {
        return EventType.PLAINMSG.getValue();
    }

    public BroadType getBroadType() {
        return broadType;
    }

    public PlainUserMsgEvent setBroadType(BroadType broadType) {
        this.broadType = broadType;
        return this;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public PlainUserMsgEvent setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    public String getReceiveRoomId() {
        return receiveRoomId;
    }

    public PlainUserMsgEvent setReceiveRoomId(String receiveRoomId) {
        this.receiveRoomId = receiveRoomId;
        return this;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public PlainUserMsgEvent setMsgContent(String msgContent) {
        this.msgContent = msgContent;
        return this;
    }

    @Override
    public String toString() {
        return "PlainMsgEvent{" +
                "broadType=" + broadType +
                ", receiverUserId=" + receiverUserId +
                ", receiveRoomId='" + receiveRoomId + '\'' +
                ", msgContent='" + msgContent + '\'' +
                '}';
    }
}
