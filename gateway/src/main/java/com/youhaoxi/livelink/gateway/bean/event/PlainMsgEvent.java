package com.youhaoxi.livelink.gateway.bean.event;

import com.youhaoxi.livelink.gateway.bean.enums.BroadType;
import com.youhaoxi.livelink.gateway.bean.enums.EventType;

public class PlainMsgEvent implements MsgEvent{
    private BroadType broadType;//1:群发 2:点对点
    private Integer receiverUserId;//接收方用户id  私聊
    private String receiveRoomId;//接收方群id  群聊
    private String msgContent;

    @Override
    public int getEventType() {
        return EventType.PLAINMSG.getValue();
    }

    public BroadType getBroadType() {
        return broadType;
    }

    public PlainMsgEvent setBroadType(BroadType broadType) {
        this.broadType = broadType;
        return this;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public PlainMsgEvent setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    public String getReceiveRoomId() {
        return receiveRoomId;
    }

    public PlainMsgEvent setReceiveRoomId(String receiveRoomId) {
        this.receiveRoomId = receiveRoomId;
        return this;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public PlainMsgEvent setMsgContent(String msgContent) {
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
