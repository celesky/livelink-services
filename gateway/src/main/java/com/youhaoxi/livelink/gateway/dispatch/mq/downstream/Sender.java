package com.youhaoxi.livelink.gateway.dispatch.mq.downstream;

/**
 * 下发给用户
 */
public interface Sender {
    void send(byte[] body);
}
