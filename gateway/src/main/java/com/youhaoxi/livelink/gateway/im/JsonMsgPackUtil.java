package com.youhaoxi.livelink.gateway.im;

import com.alibaba.fastjson.JSON;
import com.youhaoxi.livelink.gateway.im.event.EventJsonParserManager;
import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;


public class JsonMsgPackUtil {
    /**
     * eventType+json
     * @param event
     * @return
     */
    public static String pack(IMsgEvent event){
        String json = JSON.toJSONString(event);
        String str = event.getEventType()+json;
        return str;
    }


    /**
     * eventType+json
     * @param
     * @return
     */
    public static IMsgEvent unPack(String txt){
        String stype = String.valueOf(txt.charAt(0));;
        String json = txt.substring(1,txt.length());
        IMsgEvent msgEvent = EventJsonParserManager.parseJsonToEvent(Integer.parseInt(stype),json);
        return msgEvent;
    }

}
