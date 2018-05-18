package com.youhaoxi.livelink.gateway.im.handler;

import com.youhaoxi.livelink.gateway.dispatch.Worker;

public interface EventHandler {
    void execute(Worker worker);
}
