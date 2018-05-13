package com.youhaoxi.livelink.gateway.im.event;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.msg.Header;
import com.youhaoxi.livelink.gateway.im.msg.User;

//加入聊天室
public class JoinRoomEvent extends BaseEvent {
    public Integer userId;
    public String roomId;

    @Override
    public int getEventType() {
        return EventType.JOINROOM.getValue();
    }

    public int getUserId() {
        return userId;
    }

    public JoinRoomEvent setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public JoinRoomEvent setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    @Override
    public String toString() {
        return "JoinRoomEvent{" +
                "userId=" + userId +
                ", roomId='" + roomId + '\'' +
                '}';
    }


    public static void main(String[] args) {

        Header header = new Header();
        header.setUuid("adfafafsdfsfaa123132")
                .setEventType(EventType.LOGIN);

        User user = new User();
        user.setHeadImg("asdfas")
                .setName("xdsfa")
                .setUserId(111);

        JoinRoomEvent joinRoomEvent = new JoinRoomEvent();
        joinRoomEvent.setUser(user);
        joinRoomEvent.setHeader(header);

        joinRoomEvent.setRoomId("1234");
        joinRoomEvent.setUserId(1111);


        String s =JSON.toJSONString(joinRoomEvent);
        System.out.println(" s = " +  s);
    }
}
