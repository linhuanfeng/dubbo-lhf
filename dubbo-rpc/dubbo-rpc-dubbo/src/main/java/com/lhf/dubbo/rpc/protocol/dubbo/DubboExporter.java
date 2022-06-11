package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.rpc.Exporter;
import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.ProxyFactory;
import com.lhf.dubbo.rpc.proxy.jdk.JdkProxyFactory;

public class DubboExporter<T> implements Exporter<T> {
    private Invoker<T> invoker;

    public DubboExporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    public Invoker<T> getInvoker() {
        return invoker;
    }

    public void unexport() {

    }
}
