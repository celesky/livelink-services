package com.youhaoxi.livelink.gateway.bean.enums;

public enum RichMsgType {
    GIFT(1),//礼物
    PROP(2); //道具

    private int value;

    private RichMsgType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public RichMsgType setValue(int value) {
        this.value = value;
        return this;
    }
}
