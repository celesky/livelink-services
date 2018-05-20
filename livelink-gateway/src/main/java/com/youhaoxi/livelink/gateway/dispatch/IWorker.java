package com.youhaoxi.livelink.gateway.dispatch;

public interface IWorker {
    ResultMsgDispatcher getDispatcher();

    InterMsgDispatcher getInterMsgDispatcher();

}
