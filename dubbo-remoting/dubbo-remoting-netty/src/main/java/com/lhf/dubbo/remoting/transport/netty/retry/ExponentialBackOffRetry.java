package com.lhf.dubbo.remoting.transport.netty.retry;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 指数重连策略
 */
@Slf4j
public class ExponentialBackOffRetry implements RetryPolicy {
    /**
     * 最大可以重连的次数
     */
    private static final int MAX_RETRY_LIMIT = 30;
    /**
     * 默认重连最长的等待时间
     */
    private static final int DEFAULT_MAX_SLEEP_MS = Integer.MAX_VALUE;
     private final Random random = new Random();
 
    private final long baseSleepTimeMs;
    private final int maxRetries;
    private AtomicInteger curRetries=new AtomicInteger(0);
    private final int maxSleepMs;


    public ExponentialBackOffRetry() {
        this(0,3);
    }

    public ExponentialBackOffRetry(int maxRetries) {
        this(0,maxRetries);
    }

    public ExponentialBackOffRetry(int baseSleepTimeMs, int maxRetries) {
        this(baseSleepTimeMs, maxRetries, DEFAULT_MAX_SLEEP_MS);
    }
 
    public ExponentialBackOffRetry(int baseSleepTimeMs, int maxRetries, int maxSleepMs) {
        this.maxRetries = maxRetries;
        this.baseSleepTimeMs = baseSleepTimeMs;
        this.maxSleepMs = maxSleepMs;
    }
 
 
    @Override
    public boolean retry() {
        if(curRetries.intValue()>=maxRetries){
            return false;
        }
        curRetries.incrementAndGet();
        return true;
    }

    public int getCurRetries(){
        return curRetries.intValue();
    }
 
    @Override
    public long getSleepTimeMs() {
        if(curRetries.intValue()>=maxRetries){
            return -1l;
        }
        long sleepMs = baseSleepTimeMs * Math.max(1, random.nextInt(1 << curRetries.intValue()));
        if(sleepMs>maxSleepMs){
            sleepMs=maxSleepMs;
        }
 
        return sleepMs;
    }
}