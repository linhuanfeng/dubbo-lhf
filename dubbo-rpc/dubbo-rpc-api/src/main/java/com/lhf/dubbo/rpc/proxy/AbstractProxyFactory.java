package com.lhf.dubbo.rpc.proxy;

import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.ProxyFactory;

public abstract class AbstractProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return getProxy(invoker, new Class[]{invoker.getInterface()});
    }

    protected abstract  <T> T getProxy(Invoker<T> invoker, Class[] interfaces);
}
