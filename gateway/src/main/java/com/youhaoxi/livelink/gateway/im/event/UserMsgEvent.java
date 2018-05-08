package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.enums.RichMsgType;

/**
 * 用户消息事件
 * 目前两种子类
 * PlainMsgEvent
 * RichMsgEvent
 *
 * 用户这种消息  需要路由
 *
 */
public abstract class UserMsgEvent implements IMsgEvent{
    protected BroadType broadType;//1:群发 2:点对点
    protected Integer receiverUserId;//接收方用户id  私聊
    protected String receiveRoomId;//接收方群id  群聊

    public BroadType getBroadType() {
        return broadType;
    }

    public UserMsgEvent setBroadType(BroadType broadType) {
        this.broadType = broadType;
        return this;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public UserMsgEvent setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    public String getReceiveRoomId() {
        return receiveRoomId;
    }

    public UserMsgEvent setReceiveRoomId(String receiveRoomId) {
        this.receiveRoomId = receiveRoomId;
        return this;
    }
}
