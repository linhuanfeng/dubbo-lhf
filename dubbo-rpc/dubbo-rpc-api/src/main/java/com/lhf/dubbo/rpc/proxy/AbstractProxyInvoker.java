package com.lhf.dubbo.rpc.proxy;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.rpc.Invocation;
import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class AbstractProxyInvoker<T> implements Invoker<T> {
    private final T proxy;
    private final Class<T> type;
    private URL url;

    public AbstractProxyInvoker(T proxy, Class type, URL url) {
        this.proxy = proxy;
        this.type = type;
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void setUrl(URL url) {
        this.url=url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }
}
