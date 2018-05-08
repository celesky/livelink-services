package com.youhaoxi.livelink.gateway.im;

import com.alibaba.fastjson.JSONObject;
import com.youhaoxi.livelink.gateway.im.enums.EventType;
import com.youhaoxi.livelink.gateway.im.msg.Header;
import com.youhaoxi.livelink.gateway.im.msg.Msg;
import com.youhaoxi.livelink.gateway.im.msg.User;

import java.util.HashMap;

public class JsonUtil {


    public static void main(String[] args) {
        Header header = new Header();
        header.setUuid("adfafafsdfsfaa123132")
                .setEventType(EventType.LOGIN);

        User user = new User();
        user.setHeadImg("asdfas")
                .setName("xdsfa")
                .setUserId(111);

        HashMap eventMap = new HashMap();
        eventMap.put("userId",111);
        eventMap.put("sessionId","sdfafew121fdas");


//        LoginEvent event =new LoginEvent();
//        event.setSessionId("12313fafa");


        Msg msg = new Msg();
        msg.setHeader(header);
        msg.setUser(user);
        msg.setEventMap(eventMap) ;
        //msg.event=event;
        String jsonMsg = JSONObject.toJSONString(msg);
        System.out.println("jsonMsg = " + jsonMsg);
        //String jsonMsg="{\"event\":{\"sessionId\":\"222222\",\"userId\":111},\"header\":{\"eventType\":1,\"uuid\":\"12312312\"},\"user\":{\"headImg\":\"asdfas\",\"name\":\"xdsfa\",\"userId\":111}}";
        Msg um = JSONObject.parseObject(jsonMsg, Msg.class);
        //Msg um = new Gson().fromJson(jsonMsg,Msg.class);
        System.out.println("um = " + um.toString());

        //msg.event=null;
        //System.out.println("xxxxxxxxx 111111" + (msg.getEvent()==null));
        System.out.println("1123131231 = " + 1123131231);

        //int i = msg.getEvent()==null?0:1;
        //System.out.println("i = " + i);
    }
}
