package com.youhaoxi.livelink.gateway.im.msg;

import com.youhaoxi.livelink.gateway.im.enums.InterMsgType;

/**
 * 用于同步用户的连接状态给各个服务节点
 * 其实就是两个状态需要同步到每个节点
 * 一个就是用户的连接状态 userId--->host
 * 一个就是聊天室成员列表 roomId--->List<userId>
 */
public class InterMsg {
    private Integer userId;
    private InterMsgType interMsgType;

    private String host;
    private String roomId;//聊天室id



    public Integer getUserId() {
        return userId;
    }

    public InterMsg setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getHost() {
        return host;
    }

    public InterMsg setHost(String host) {
        this.host = host;
        return this;
    }

    public InterMsgType getInterMsgType() {
        return interMsgType;
    }

    public InterMsg setInterMsgType(InterMsgType interMsgType) {
        this.interMsgType = interMsgType;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public InterMsg setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }
}
