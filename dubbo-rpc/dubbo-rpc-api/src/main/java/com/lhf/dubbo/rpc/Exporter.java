package com.lhf.dubbo.rpc;

/**
 * 服务暴露的封装的对象
 * @param <T>
 */
public interface Exporter<T> {
    Invoker<T> getInvoker();
    void unexport();
}
