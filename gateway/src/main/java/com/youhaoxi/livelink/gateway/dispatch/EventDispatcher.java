package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.im.event.IMsgEvent;

/**
 * 原始用户消息事件派发器
 */
public interface EventDispatcher extends Dispatcher{

    void dispatch(IMsgEvent msgEvent);

    void groupDispatch(IMsgEvent msgEvent,String roomId);
}
