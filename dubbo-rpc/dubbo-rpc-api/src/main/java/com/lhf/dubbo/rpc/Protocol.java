package com.lhf.dubbo.rpc;

import com.lhf.dubbo.common.bean.URL;

/**
 * 封装 RPC 调用，以 Invocation, Result 为中心
 */
public interface Protocol {
    /**
     * 将服务暴露出去
     * @param invoker 把服务封装成一个invoker
     * @param <T>
     * @return
     */
    <T> Exporter<T> export(Invoker<T> invoker) throws Throwable;

    /**
     * 引用一个远程dubbo服务
     * @param type
     * @param url
     * @return
     */
    Object refer(Class type, URL url) throws Throwable;
}
