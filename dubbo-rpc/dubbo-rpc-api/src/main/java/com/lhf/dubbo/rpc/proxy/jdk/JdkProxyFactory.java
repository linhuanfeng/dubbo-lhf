package com.lhf.dubbo.rpc.proxy.jdk;

import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.proxy.AbstractProxyFactory;
import com.lhf.dubbo.rpc.proxy.AbstractProxyInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyFactory extends AbstractProxyFactory {
    @Override
    public  <T> T getProxy(Invoker<T> invoker, Class[] interfaces) {
        return (T) Proxy.newProxyInstance(invoker.getInterface().getClassLoader(), interfaces,
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(proxy,args);
                    }
                });
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        return new AbstractProxyInvoker<T>(proxy,type,url){
            @Override
            public Object invoke(RpcRequest request) {
                try {
                    Method method = proxy.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
                    return method.invoke(proxy,request.getArguments());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
