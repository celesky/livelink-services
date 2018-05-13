package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;

public interface Dispatcher {

    void dispatch(IMsgEvent msgEvent);

}
