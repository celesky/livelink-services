package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.im.msg.InterMsg;
import com.youhaoxi.livelink.gateway.im.msg.ResultMsg;

/*
    内部消息派发
    比如用户上下线的消息通知
 */
public interface InterMsgDispatcher {
    void dispatch(InterMsg msg);
}
