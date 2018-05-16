package com.youhaoxi.livelink.gateway;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.Dispatcher;
import com.youhaoxi.livelink.gateway.dispatch.Worker;
import com.youhaoxi.livelink.gateway.dispatch.mq.RabbitConnectionManager;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.UpstreamMqTestDispatcher;
import com.youhaoxi.livelink.gateway.im.handler.TestEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadTest.class);
    static List<AtomicLong> list = new ArrayList<>();
    static volatile boolean contin = true;

    public static void test(){
//        Worker.startWorker(Constants.workerNum, UpstreamMqTestDispatcher.class, LinkedBlockingDeque.class);
//        TestEventHandler handler = null;
//        Long start = Instant.now().toEpochMilli();
//        //logger.info("start:"+ start);
//        for(int i=0;i<100000;i++){
//            //logger.info("i:"+ i);
//            handler = new TestEventHandler(null,null);
//            Worker.dispatch(i,handler);
//        }
//        Long end = Instant.now().toEpochMilli();
//        logger.info("end:"+ end);
//
//        logger.info("time = " + (end-start)/1000);

    }

    public static void main(String[] args) {
        test();
        //testPool();
    }

    public static void testPool(){
        CountDownLatch latch = new CountDownLatch(4);

        ThreadPoolExecutor pool = new  ThreadPoolExecutor(4,4,60, TimeUnit.SECONDS,new LinkedBlockingDeque<>());

        List<Dispatcher> dispatcherList = new ArrayList<Dispatcher>();
        for(int i=0;i<4;i++){
            dispatcherList.add(new UpstreamMqTestDispatcher());
            list.add(new AtomicLong(0));
        }
        Long start = Instant.now().toEpochMilli();
        for(int i=0;i<100000;i++){
            //System.out.println("i%4 = " + i%4);
            Dispatcher dispatcher = dispatcherList.get(i%4);
            AtomicLong atomicLong = list.get(i%4);
            pool.submit(new Task(dispatcher,atomicLong));
        }

        while(contin){
            long sum = list.stream().map(e->e.incrementAndGet()).reduce(Long::sum).get();
            System.out.println("sum = " + sum);
            if(sum<100000){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                contin = false;
            }
        }
        RabbitConnectionManager.getInstance().closeConnection();
        Long end = Instant.now().toEpochMilli();
        logger.info("end:"+ end);
        logger.info("time = " + (end-start)/1000);

    }

    static class Task implements Runnable{
        Dispatcher dispatcher = null;
        AtomicLong atomicLong ;
        public Task(Dispatcher dispatcher,AtomicLong atomicLong){
            this.dispatcher = dispatcher;
            this.atomicLong = atomicLong;
        }
        @Override
        public void run() {
            //dispatcher.dispatch(new Msg());
            atomicLong.incrementAndGet();
        }
    }


}
