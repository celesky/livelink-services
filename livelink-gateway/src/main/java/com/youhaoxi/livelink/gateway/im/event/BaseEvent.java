package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.msg.Header;
import com.youhaoxi.livelink.gateway.im.msg.User;

public abstract class BaseEvent implements IMsgEvent {

    public Header header;
    public User from;

    @Override
    public int getUserId() {
        return from.getUserId();
    }

    public Header getHeader() {
        return header;
    }

    public BaseEvent setHeader(Header header) {
        this.header = header;
        return this;
    }

    public User getFrom() {
        return from;
    }

    public BaseEvent setFrom(User from) {
        this.from = from;
        return this;
    }
}
