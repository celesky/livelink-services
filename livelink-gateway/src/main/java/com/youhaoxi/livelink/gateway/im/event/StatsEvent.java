package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.enums.EventType;

/**
 * 运行时数据查询事件
 */
public class StatsEvent extends BaseEvent{

    private Integer action;//1查询用户  2查询房间
    //查询的用户id
    private Integer qUserId;

    private String qRoomId;

    private Integer roomMemnberStart;

    private Integer roomMemnberEnd;

    @Override
    public int getEventType() {
        return EventType.STATS.getValue();
    }

    public Integer getAction() {
        return action;
    }

    public StatsEvent setAction(Integer action) {
        this.action = action;
        return this;
    }

    public Integer getqUserId() {
        return qUserId;
    }

    public StatsEvent setqUserId(Integer qUserId) {
        this.qUserId = qUserId;
        return this;
    }

    public Integer getRoomMemnberStart() {
        return roomMemnberStart;
    }

    public StatsEvent setRoomMemnberStart(Integer roomMemnberStart) {
        this.roomMemnberStart = roomMemnberStart;
        return this;
    }

    public Integer getRoomMemnberEnd() {
        return roomMemnberEnd;
    }

    public StatsEvent setRoomMemnberEnd(Integer roomMemnberEnd) {
        this.roomMemnberEnd = roomMemnberEnd;
        return this;
    }

    public String getqRoomId() {
        return qRoomId;
    }

    public StatsEvent setqRoomId(String qRoomId) {
        this.qRoomId = qRoomId;
        return this;
    }
}
