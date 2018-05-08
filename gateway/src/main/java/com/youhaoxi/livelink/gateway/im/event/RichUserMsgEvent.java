package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.BroadType;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.enums.RichMsgType;

/**
 * 礼物道具事件 定义
 */
public class RichUserMsgEvent extends UserMsgEvent {

    public RichMsgType richMsgType;//1:道具 2:礼物
    public Integer itemId;//道具或者礼物id
    public String ItemName;//名称
    public String ItemImgUrl;//图片url
    public Integer itemCount;//道具数量

    @Override
    public int getEventType() {
        return EventType.RICHMSG.getValue();
    }

    public RichMsgType getRichMsgType() {
        return richMsgType;
    }

    public RichUserMsgEvent setRichMsgType(RichMsgType richMsgType) {
        this.richMsgType = richMsgType;
        return this;
    }

    public BroadType getBroadType() {
        return broadType;
    }

    public RichUserMsgEvent setBroadType(BroadType broadType) {
        this.broadType = broadType;
        return this;
    }

    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    public RichUserMsgEvent setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
        return this;
    }

    public String getReceiveRoomId() {
        return receiveRoomId;
    }

    public RichUserMsgEvent setReceiveRoomId(String receiveRoomId) {
        this.receiveRoomId = receiveRoomId;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public RichUserMsgEvent setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return ItemName;
    }

    public RichUserMsgEvent setItemName(String itemName) {
        ItemName = itemName;
        return this;
    }

    public String getItemImgUrl() {
        return ItemImgUrl;
    }

    public RichUserMsgEvent setItemImgUrl(String itemImgUrl) {
        ItemImgUrl = itemImgUrl;
        return this;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public RichUserMsgEvent setItemCount(Integer itemCount) {
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
