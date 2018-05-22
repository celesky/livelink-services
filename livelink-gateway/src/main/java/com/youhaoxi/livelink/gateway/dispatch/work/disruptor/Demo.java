package com.youhaoxi.livelink.gateway.dispatch.work.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.time.Instant;
import java.util.concurrent.ThreadFactory;

public class Demo {
    static int i=1;

    public static void main(String[] args) throws Exception {
        // 队列中的元素
        class Element {
            private int value;

            public int get() {
                return value;
            }

            public void set(int value) {
                this.value = value;
            }
        }

        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "simpleThread");
            }
        };

        // RingBuffer生产工厂,初始化RingBuffer的时候使用
        EventFactory<Element> factory = new EventFactory<Element>() {
            @Override
            public Element newInstance() {
                return new Element();
            }
        };

        // 处理Event的handler
        EventHandler<Element> handler = new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch) {
                //System.out.println("Element: " + element.get());
                if(i==1){
                    System.out.println("start  element = " + element.get());

                }
                i++;
                if(i>=1000000){
                    System.out.println("end  element = " + element.get());
                }
            }
        };

        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();

        // 指定RingBuffer的大小
        int bufferSize = 1024;

        // 创建disruptor，采用单生产者模式
        Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

        // 设置EventHandler
        disruptor.handleEventsWith(handler);

        // 启动disruptor的线程
        disruptor.start();

        RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();
        long start = Instant.now().toEpochMilli();

        for (int l = 0; l<1000000; l++) {
            // 获取下一个可用位置的下标
//            long sequence = ringBuffer.next();
//            try {
//                // 返回可用位置的元素
//                Element event = ringBuffer.get(sequence);
//                // 设置该位置元素的值
//                event.set(l);
//            } finally {
//                ringBuffer.publish(sequence);
//            }
            //Thread.sleep(10);
            int k = l;
            ringBuffer.publishEvent((event, sequence) -> event.set(k));

        }
        System.out.println("time:" + (Instant.now().toEpochMilli()-start));
    }
}