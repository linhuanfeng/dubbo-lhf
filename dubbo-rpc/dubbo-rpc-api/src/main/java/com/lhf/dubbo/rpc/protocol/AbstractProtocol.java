package com.lhf.dubbo.rpc.protocol;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.rpc.*;

public abstract class AbstractProtocol implements Protocol {
    protected ExchangeServer exchangeServer;
    protected ExchangeClient exchangeClient;
    protected ProtocolServer protocolServer;
    protected ProtocolClient protocolClient; // 对NettyClient的装饰，发送数据

    // 创建ExchangeServer
    protected void openServer(URL url) throws Throwable {
        if(protocolServer==null){
            protocolServer=createServer(url);
        }
    }

    protected void openClient(URL url) throws Throwable {
        if(protocolClient==null){
            protocolClient=createClient(url);
        }
    }

    protected abstract ProtocolServer createServer(URL url) throws Throwable;
    protected abstract ProtocolClient createClient(URL url) throws Throwable;
    protected static String serviceKey(URL url) {
        return ProtocolUtils.serviceKey(url);
    }
}
