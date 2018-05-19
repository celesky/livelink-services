package com.youhaoxi.livelink.gateway.dispatch.disruptor;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.handler.EventHandler;

/**
 * 队列元素
 */
public class Element {
    private EventHandler value;

    public EventHandler getValue() {
        return value;
    }

    public Element setValue(EventHandler value) {
        this.value = value;
        return this;
    }
}
