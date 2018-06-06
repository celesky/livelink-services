package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.im.msg.EndpointMsg;

/**
 *  这个消息将会直接下发给用户
 */
public interface ResultMsgDispatcher extends Dispatcher{

    void dispatch(EndpointMsg msg);

    void groupDispatch(EndpointMsg msg, String roomId);
}
