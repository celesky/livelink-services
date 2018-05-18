package com.youhaoxi.livelink.gateway.im.event;

public class TestEvent extends UserMsgEvent{
    public String msgContent;
    @Override
    public int getEventType() {
        return 0;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public TestEvent setMsgContent(String msgContent) {
        this.msgContent = msgContent;
        return this;
    }
}
