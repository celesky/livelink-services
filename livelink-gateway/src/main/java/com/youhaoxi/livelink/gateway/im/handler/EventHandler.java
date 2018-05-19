package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.IWorker;

public interface EventHandler {
    void execute(IWorker worker);
}
