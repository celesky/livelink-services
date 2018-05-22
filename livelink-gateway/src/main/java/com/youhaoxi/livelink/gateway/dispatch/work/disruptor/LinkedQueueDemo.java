package com.youhaoxi.livelink.gateway.dispatch.work.disruptor;

import com.youhaoxi.livelink.gateway.dispatch.mq.im.upstream.MqRstMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.mq.inter.upstream.MqInterMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.work.DisruptorWorker;
import com.youhaoxi.livelink.gateway.dispatch.work.Worker;
import com.youhaoxi.livelink.gateway.im.handler.DemoEventHandler;
import com.youhaoxi.livelink.gateway.im.handler.TestEventHandler;

import java.time.Instant;

public class LinkedQueueDemo {

    public static void main(String[] args) {
        DisruptorWorker.startWorker(4,MqRstMsgDispatcher.class, MqInterMsgDispatcher.class);
        Worker.startWorker(4,MqRstMsgDispatcher.class, MqInterMsgDispatcher.class);

        LinkedQueueDemo.linkqueue();
        LinkedQueueDemo.linkqueue();
        LinkedQueueDemo.linkqueue();



        LinkedQueueDemo.disruptor();
        LinkedQueueDemo.disruptor();
        LinkedQueueDemo.disruptor();
        LinkedQueueDemo.disruptor();
        LinkedQueueDemo.disruptor();






        DisruptorWorker.shutdown();
        Worker.shutdown();
    }
    public static void disruptor(){

        long start = Instant.now().toEpochMilli();
        for(int i=0;i<20000000;i++){
            DisruptorWorker.dispatch(i,new DemoEventHandler(null,null));
        }

        System.out.println("time:" + (Instant.now().toEpochMilli()-start));
    }
    public static void linkqueue() {

        long start = Instant.now().toEpochMilli();
        for(int i=0;i<20000000;i++){
            Worker.dispatch(i,new DemoEventHandler(null,null));
        }

        System.out.println("time:" + (Instant.now().toEpochMilli()-start));
    }

}
