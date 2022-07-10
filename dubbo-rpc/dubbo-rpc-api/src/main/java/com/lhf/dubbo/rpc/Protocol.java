package com.lhf.dubbo.rpc;

import com.lhf.dubbo.common.bean.URL;

import java.util.List;


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
     * 供registryProtocol使用
     * 引用一个远程dubbo服务
     * @param type
     * @param url 根据url去注册中心获取真正的url
     * @return
     */
    default Object refer(Class type,URL url) throws Throwable{
        throw new UnsupportedOperationException();
    }

    /**
     * 供dubboProtocol使用
     * @param type
     * @param urls
     * @return
     * @throws Throwable
     */
    default Object refer(Class type, List<URL> urls) throws Throwable{
        throw new UnsupportedOperationException();
    }
}
