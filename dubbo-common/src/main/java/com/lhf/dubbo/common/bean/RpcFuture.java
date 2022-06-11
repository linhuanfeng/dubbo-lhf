package com.lhf.dubbo.common.bean;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class RpcFuture implements Future<Object>, Serializable {
    private final RpcRequest rpcRequest;
    private RpcResponse rpcResponse;
    private Syn syn;
    class Syn extends AbstractQueuedSynchronizer{
        public boolean isSignalled(){ // 大于0表示接收到通知
            return getState()!=0;
        }

        @Override
        protected int tryAcquireShared(int arg) { // 大于0表示可以获取成功
            return isSignalled()?1:-1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) { // 是否资源
            setState(1);
            return true;
        }
    }

    public RpcFuture(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        this.syn=new Syn();
    }

    public RpcRequest getRpcRequest() {
        return rpcRequest;
    }

    public RpcResponse getRpcResponse() {
        return rpcResponse;
    }

    public void setRpcResponse(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        syn.releaseShared(1); // 共享释放资源
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        throw new UnsupportedOperationException("暂不支持此方法");
    }

    public boolean isDone() {
        return syn.isSignalled();
    }

    public Object get() throws InterruptedException {
        syn.acquireSharedInterruptibly(1); // 可中断共享获取资源
        if(rpcResponse!=null){
            return rpcResponse.getResult();
        }
        return null;
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
