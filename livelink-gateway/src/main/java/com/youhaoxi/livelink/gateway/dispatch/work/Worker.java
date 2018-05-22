package com.youhaoxi.livelink.gateway.dispatch.work;

import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.dispatch.InterMsgDispatcher;
import com.youhaoxi.livelink.gateway.dispatch.ResultMsgDispatcher;
import com.youhaoxi.livelink.gateway.im.handler.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 工作线程 做消息派发事情
 * 队列使用BlockingQueue
 */
public class Worker extends Thread implements IWorker {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    public static Worker[] _workers;
    //每个worker绑定一个 dispatcher-->RabbitProducer--->1个rabbitChannel
    private ResultMsgDispatcher dispatcher ;
    //内部通信消息派发器
    public InterMsgDispatcher interDispatcher ;

    private static Random rnd ;

    protected  volatile boolean _stop =false;
    private  BlockingQueue<EventHandler> taskQueue = new LinkedBlockingDeque<>(2048);

    private static int workerNum;


    public Worker(ResultMsgDispatcher dispatcher ,InterMsgDispatcher intnerDispatcher){
        this.dispatcher = dispatcher;
        this.interDispatcher = interDispatcher;
    }

    @Override
    public ResultMsgDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public InterMsgDispatcher getInterMsgDispatcher() {
        return interDispatcher;
    }


    public static void  startWorker(int workNum, Class<? extends ResultMsgDispatcher> dispatcherClazz
            ,Class<? extends InterMsgDispatcher> interDispatcherClazz) {
        workerNum = workNum;
        rnd = new Random(workNum);
        _workers = new Worker[workNum];
        for(int i = 0; i < workNum; i++) {
            ResultMsgDispatcher dispatcher = null;
            InterMsgDispatcher interMsgDispatcher = null;
            try {
                dispatcher = dispatcherClazz.getConstructor().newInstance();
                interMsgDispatcher = interDispatcherClazz.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            _workers[i] = new Worker(dispatcher,interMsgDispatcher);
            _workers[i].start();
        }
    }

    public static void stopWorkers() {
        for(int i = 0; i < workerNum; i++) {
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
                logger.error("Caught Exception",e);
            } finally {

            }
        }
    }

    public static int getWorkId(Integer str) {
        if(str==null){
            //取随机数
            str = rnd.nextInt();
        }
        return str.hashCode() % workerNum;
    }

    public static void shutdown(){
        if(_workers!=null&&_workers.length>0){
            for(int i = 0;i<_workers.length;i++){
                _workers[i]._stop=true;
            }
        }

    }


    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            System.out.println("str = rnd.nextInt(); = " + rnd.nextInt());
        }
    }




}
