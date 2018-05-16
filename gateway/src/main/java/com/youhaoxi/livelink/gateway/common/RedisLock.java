package com.youhaoxi.livelink.gateway.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    //用户redis数据操作锁 用户在连接,登录,登出,等操作时可能发生并发情况,对其加redis锁
    public final static String USER_REDIS_LOCK_KEY="user_redis_lock#%s";

    private String lockKey;

    private boolean isLock=false;

    public RedisLock(String lockKey,String... value){
        this.lockKey = String.format(lockKey,value);
    }

    /**
     * redis key超时时间
     * @param second
     * @return
     */
    public boolean tryLock(int second){

        long r = RedisUtil.cache().setnx(lockKey,"1");
        RedisUtil.cache().expire(lockKey,second);
        if(r>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 自旋时间
     * @param timeoutMilli
     */
    public void multitryLock(int timeoutMilli){
        isLock = tryLock((timeoutMilli/1000)+2);
        long last = Instant.now().toEpochMilli();
        while(!isLock){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                logger.error("multitryLock sleep error:{}",e);
            }
            if(Instant.now().toEpochMilli()-last>timeoutMilli){
                //锁超时
                throw new RuntimeException("multi retry lock timeout!");
            }
            //再次尝试 重新获取锁
            isLock=tryLock(timeoutMilli/1000);
        }
    }


    public void unlock(){
        if(isLock){
            RedisUtil.cache().del(lockKey);
            isLock=false;
        }
    }
}
