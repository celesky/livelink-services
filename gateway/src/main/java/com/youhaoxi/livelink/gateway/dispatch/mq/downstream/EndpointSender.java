package com.youhaoxi.livelink.gateway.dispatch.mq.downstream;

import java.io.UnsupportedEncodingException;

public class EndpointSender implements Sender{

    @Override
    public void send(byte[] body) {
        try {
            String message = new String(body, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
