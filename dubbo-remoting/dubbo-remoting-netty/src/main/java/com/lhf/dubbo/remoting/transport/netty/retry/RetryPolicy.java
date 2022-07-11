package com.lhf.dubbo.remoting.transport.netty.retry;

public interface RetryPolicy {
    boolean retry();
    int getCurRetries();
    //获取重连需要等待的时间
    long getSleepTimeMs();
}