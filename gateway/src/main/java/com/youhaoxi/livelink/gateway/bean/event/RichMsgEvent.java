package com.youhaoxi.livelink.gateway.bean.event;

import com.youhaoxi.livelink.gateway.bean.enums.BroadType;
import com.youhaoxi.livelink.gateway.bean.enums.EventType;
import com.youhaoxi.livelink.gateway.bean.enums.RichMsgType;

//礼物道具事件
public class RichMsgEvent implements MsgEvent{
    private RichMsgType richMsgType;//1:礼物 2:道具
    private BroadType broadType;//1:群发 2:点对点
    private Integer receiverUserId;//接收方用户id  私聊
    private String receiveRoomId;//接收方群id  群聊

    private Integer itemId;//
    private String ItemName;
    private String ItemImgUrl;
    private Integer itemCount;

    @Override
    public int getEventType() {
        return EventType.RICHMSG.getValue();
    }

    public RichMsgType getRichMsgType() {
        return richMsgType;
    }

    public RichMsgEvent setRichMsgType(RichMsgType richMsgType) {
        this.richMsgType = richMsgType;
        return this;
    }

    public BroadType getBroadType() {
        return broadType;
    }

    public RichMsgEvent setBroadType(BroadType broadType) {
        this.broadType = broadType;
        return this;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public RichMsgEvent setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    public String getReceiveRoomId() {
        return receiveRoomId;
    }

    public RichMsgEvent setReceiveRoomId(String receiveRoomId) {
        this.receiveRoomId = receiveRoomId;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public RichMsgEvent setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return ItemName;
    }

    public RichMsgEvent setItemName(String itemName) {
        ItemName = itemName;
        return this;
    }

    public String getItemImgUrl() {
        return ItemImgUrl;
    }

    public RichMsgEvent setItemImgUrl(String itemImgUrl) {
        ItemImgUrl = itemImgUrl;
        return this;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public RichMsgEvent setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
        return this;
    }


    @Override
    public String toString() {
        return "RichMsgEvent{" +
                "richMsgType=" + richMsgType +
                ", receiverUserId=" + receiverUserId +
                ", receiveRoomId='" + receiveRoomId + '\'' +
                ", itemId=" + itemId +
                ", ItemName='" + ItemName + '\'' +
                ", ItemImgUrl='" + ItemImgUrl + '\'' +
                ", itemCount=" + itemCount +
                '}';
    }
}
