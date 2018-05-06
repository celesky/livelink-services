package com.youhaoxi.livelink.gateway.bean.enums;

/**
 *
 */
public enum BroadType {
    MASS(1),//群发
    P2P(2); //点对点
    private int value;


    private BroadType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public BroadType setValue(int value) {
        this.value = value;
        return this;
    }

}
