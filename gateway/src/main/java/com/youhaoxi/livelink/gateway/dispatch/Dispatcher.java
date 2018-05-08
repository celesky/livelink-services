package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;
import com.youhaoxi.livelink.gateway.im.msg.IMsg;

public interface Dispatcher {

    void dispatch(IMsg msgEvent);

}
