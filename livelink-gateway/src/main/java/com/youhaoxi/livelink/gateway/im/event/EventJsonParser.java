package com.youhaoxi.livelink.gateway.im.event;

public interface EventJsonParser {
    //eventjson转换成msgEvent对象
    IMsgEvent parse(String eventJson);

}
