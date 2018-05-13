package com.youhaoxi.livelink.gateway.im.event;

import com.youhaoxi.livelink.gateway.im.msg.Header;
import com.youhaoxi.livelink.gateway.im.msg.User;

public abstract class BaseEvent implements IMsgEvent{

    protected Header header;
    protected User user;

    public int getUserId(){
        return user.getUserId();
    }
    public Header getHeader() {
        return header;
    }

    public BaseEvent setHeader(Header header) {
        this.header = header;
        return this;
    }

    public User getUser() {
        return user;
    }

    public BaseEvent setUser(User user) {
        this.user = user;
        return this;
    }
}
