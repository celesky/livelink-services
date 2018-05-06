package com.youhaoxi.livelink.gateway.bean.enums;

/**
 * 事件类型 1:登录. 2:加入聊天室 3:发送文字消息 4:发送道具\礼物消息 5:退出聊天室
 */
public enum EventType {
    LOGIN(1), JOINROOM(2), PLAINMSG(3), RICHMSG( 4),QUITROOM(5),LOGOUT(6);
    private int value;

    private EventType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public EventType setValue(int value) {
        this.value = value;
        return this;
    }
}
