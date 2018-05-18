package com.youhaoxi.livelink.gateway.im.msg;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 消息协议组成
 * 包括
 * header 头信息
 * user 用户信息
 * event 事件信息
 */
public class Msg implements IMsg,Serializable{
    public Header header;
    public User from;
    public IMsgEvent event;
    public HashMap eventMap;//只是用来接收用户的消息事件部分,接收后转换成对应的msgEvent

    public Header getHeader() {
        return header;
    }

    public Msg setHeader(Header header) {
        this.header = header;
        return this;
    }

    public User getFrom() {
        return from;
    }

    public Msg setFrom(User from) {
        this.from = from;
        return this;
    }

    public HashMap getEventMap() {
        return eventMap;
    }

    public Msg setEventMap(HashMap eventMap) {
        this.eventMap = eventMap;
        return this;
    }

    public IMsgEvent getEvent() {
        return event;
    }

    public Msg setEvent(IMsgEvent event) {
        this.event = event;
        return this;
    }


    @Override
    public String toString() {
        return "Msg{" +
                "header=" + header +
                ", from=" + from +
                ", event=" + event +
                ", eventMap=" + eventMap +
                '}';
    }
}

