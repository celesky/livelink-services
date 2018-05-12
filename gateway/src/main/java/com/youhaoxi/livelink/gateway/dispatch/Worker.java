package com.youhaoxi.livelink.gateway.dispatch;

import com.youhaoxi.livelink.gateway.common.Constants;
import com.youhaoxi.livelink.gateway.dispatch.mq.upstream.UpstreamMqDispatcher;
import com.youhaoxi.livelink.gateway.im.handler.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 工作线程 做消息派发事情
 */
public class Worker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    public static Worker[] _workers;
    //每个worker绑定一个 dispatcher-->RabbitProducer--->1个rabbitChannel
    public Dispatcher dispatcher ;

    protected  volatile boolean _stop =false;
    private  BlockingQueue<EventHandler> taskQueue ;


    public Worker(Dispatcher dispatcher,BlockingQueue<EventHandler> taskQueue){
        this.dispatcher = dispatcher;
        this.taskQueue = taskQueue;
    }



    public static void  startWorker(int workNum, Class<? extends Dispatcher> dispatcherClazz , Class<? extends BlockingQueue> taskQueueClazz) {
        _workers = new Worker[workNum];
        for(int i = 0; i < workNum; i++) {
            Dispatcher dispatcher = null;
            BlockingQueue taskQueue = null;
            try {
                dispatcher = dispatcherClazz.getConstructor().newInstance();
                taskQueue = taskQueueClazz.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            _workers[i] = new Worker(dispatcher,taskQueue);
            _workers[i].start();
        }
    }

    public static void stopWorkers() {
        for(int i = 0; i < Constants.workerNum; i++) {
            _workers[i]._stop = true;
        }
    }


    public static void dispatch(Integer userId, EventHandler handler) {
        int workId = getWorkId(userId);
        if(handler == null) {
            logger.error("handler is null");
            return;
        }
        _workers[workId].taskQueue.offer(handler);
    }

    @Override
    public void run() {
        while(!_stop) {
            EventHandler handler = null;
            try {
                handler = taskQueue.poll(600, TimeUnit.MILLISECONDS);
                if(handler == null)
                    continue;
            } catch (InterruptedException e) {
                logger.error("Caught Exception");
            }
            try {
                assert handler != null;
                //handler._jedis = AuthStarter.e_redisPoolManager.getJedis();
                handler.execute(this);
            } catch (Exception e) {
                logger.error("Caught Exception");
            } finally {

            }
        }
    }

    public static int getWorkId(Integer str) {
        return str.hashCode() % Constants.workerNum;
    }


}
