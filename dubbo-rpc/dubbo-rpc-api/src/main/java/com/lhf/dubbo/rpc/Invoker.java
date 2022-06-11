package com.lhf.dubbo.rpc;

import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;

/**
 * invoker是dubbo的核心模型，可对它发起invoke()调用。是接口实现类的代理对象
 * @param <T>
 */
public interface Invoker<T> {
    URL getUrl();
    void setUrl(URL url);
    Class<T> getInterface();
//    Result invoke(Invocation invocation);
    Object invoke(RpcRequest request);
//    Channel getChannel();
}
