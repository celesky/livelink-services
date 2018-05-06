package com.youhaoxi.livelink.gateway.bean.msg;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.bean.enums.EventType;
import com.youhaoxi.livelink.gateway.bean.event.MsgEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class UserMsg implements Serializable{
    public Header header;
    public User user;
    public MsgEvent event;

    HashMap eventMap;//只是用来接收用户的消息事件部分,接收后转换成对应的msgEvent

    public Header getHeader() {
        return header;
    }

    public UserMsg setHeader(Header header) {
        this.header = header;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserMsg setUser(User user) {
        this.user = user;
        return this;
    }

    public HashMap getEventMap() {
        return eventMap;
    }

    public UserMsg setEventMap(HashMap eventMap) {
        this.eventMap = eventMap;
        return this;
    }

    public MsgEvent getEvent() {
        return event;
    }

    public UserMsg setEvent(MsgEvent event) {
        this.event = event;
        return this;
    }

    @Override
    public String toString() {
        return "UserMsg{" +
                "header=" + header.toString() +
                ", user=" + user.toString() +
                ", event=" + event.toString()+
                ", eventMap=" + eventMap==null?"null":eventMap.toString() +
                '}';
    }

    public static void main(String[] args) {
//        LoginEvent event = new LoginEvent();
//        event.setUserId(111);
//        event.setSessionId("222222");
//        Header header = new Header();
//        header.setUuid("12312312");
//        header.setEventType(1);
//        User user = new User();
//        user.setHeadImg("asdfas").setName("xdsfa").setUserId(111);
//        UserMsg userMsg = new UserMsg();
//        userMsg.header=header;
//        userMsg.user=user;
//        userMsg.event=event;
//
//        String jsonMsg = JSONObject.toJSONString(userMsg);
//        System.out.println("jsonMsg = " + jsonMsg);
        String jsonMsg="{\"event\":{\"sessionId\":\"222222\",\"userId\":111},\"header\":{\"eventType\":1,\"uuid\":\"12312312\"},\"user\":{\"headImg\":\"asdfas\",\"name\":\"xdsfa\",\"userId\":111}}";
        UserMsg um = JSONObject.parseObject(jsonMsg, UserMsg.class);

        System.out.println("um = " + um.toString());

    }
}

