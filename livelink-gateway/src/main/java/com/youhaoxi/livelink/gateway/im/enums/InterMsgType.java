package com.youhaoxi.livelink.gateway.im.enums;

public enum InterMsgType {
    linked(1),//连接host
    unlinked(2),//断开host
    joinRoom(3),//加入房间
    quitRoom(4),//退出房间
    delRoom(5);//删除房间

    private int value;

    private InterMsgType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public InterMsgType setValue(int value) {
        this.value = value;
        return this;
    }
}
