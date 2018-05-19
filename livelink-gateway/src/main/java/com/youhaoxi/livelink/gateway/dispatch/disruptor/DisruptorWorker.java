package com.youhaoxi.livelink.gateway.dispatch.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.youhaoxi.livelink.gateway.dispatch.IWorker;
import com.youhaoxi.livelink.gateway.dispatch.ResultMsgDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.concurrent.ThreadFactory;

/**
 * 这个使用disruptor做queue,以提高内部queue的效率
 * 而worker使用BlockingQueue
 */
public class DisruptorWorker implements IWorker{
    private static final Logger logger = LoggerFactory.getLogger(DisruptorWorker.class);
    public static DisruptorWorker[] disruptors;
    private static Random rnd ;
    private static int workerNum;

    private Disruptor<Element> disruptor;
    public ResultMsgDispatcher dispatcher ;

    public DisruptorWorker _self;//自身的引用

    @Override
    public ResultMsgDispatcher getDispatcher() {
        return dispatcher;
    }

    public static void dispatch(Integer userId, com.youhaoxi.livelink.gateway.im.handler.EventHandler handler) {
        int workId = getWorkId(userId);
        if(handler == null) {
            logger.error("handler is null");
            return;
        }
        RingBuffer<Element> ringBuffer = disruptors[workId].disruptor.getRingBuffer();
        // 获取下一个可用位置的下标
        long sequence = ringBuffer.next();
        try {
            // 返回可用位置的元素
            Element event = ringBuffer.get(sequence);
            // 设置该位置元素的值
            event.setValue(handler);
        } finally {
            ringBuffer.publish(sequence);
        }

    }


    public DisruptorWorker(ResultMsgDispatcher dispatcher){
        this.dispatcher = dispatcher;
        initDisruptor();//初始化disruptor
        _self=this;
    }

    public static void  startWorker(int workNum, Class<? extends ResultMsgDispatcher> dispatcherClazz) {
        workerNum = workNum;
        rnd = new Random(workNum);
        disruptors = new DisruptorWorker[workNum];//工作组
        for(int i = 0; i < workNum; i++) {
            ResultMsgDispatcher dispatcher = null;
            try {
                dispatcher = dispatcherClazz.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            disruptors[i] = new DisruptorWorker(dispatcher);
            disruptors[i].start();
        }
    }

    public Disruptor initDisruptor(){
        // 阻塞策略
        BlockingWaitStrategy strategy = new BlockingWaitStrategy();
        // 指定RingBuffer的大小
        int bufferSize = 1024;
        // 创建disruptor，采用单生产者模式
        disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);
        // 设置 处理Event的handler
        disruptor.handleEventsWith(new EventHandler<Element>() {
            @Override
            public void onEvent(Element element, long sequence, boolean endOfBatch) {
                logger.info("Element: " + element.getValue());
                com.youhaoxi.livelink.gateway.im.handler.EventHandler messageHandler = element.getValue();
                try {
                    assert messageHandler != null;
                    messageHandler.execute(_self);
                } catch (Exception e) {
                    logger.error("handleEventsWith Exception",e);
                } finally {

                }
            }
        });

        return disruptor;
    }
    public void start(){
        // 启动disruptor的线程
        disruptor.start();
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

    public static int getWorkId(Integer str) {
        if(str==null){
            //取随机数
            str = rnd.nextInt();
        }
        return str.hashCode() % workerNum;
    }



}