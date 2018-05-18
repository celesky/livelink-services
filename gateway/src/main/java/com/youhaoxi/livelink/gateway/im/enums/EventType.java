package com.youhaoxi.livelink.gateway.im.enums;

/**
 * 事件类型 1:登录. 2:加入聊天室 3:发送文字消息 4:发送道具\礼物消息 5:退出聊天室
 */
public enum EventType {
    TEST(0),
    LOGIN(1),
    CREATEROOM(2),
    JOINROOM(3),
    QUITROOM(4),
    PLAINMSG(5),
    RICHMSG(6),
    LOGOUT(7),
    STATS(9)
    ;
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
