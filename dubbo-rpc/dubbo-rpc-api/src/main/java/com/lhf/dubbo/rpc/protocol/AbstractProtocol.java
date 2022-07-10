package com.lhf.dubbo.rpc.protocol;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public abstract class AbstractProtocol implements Protocol {
    protected ExchangeServer exchangeServer;
//    protected ExchangeClient exchangeClient;
    protected ProtocolServer protocolServer;

//    protected ProtocolClient protocolClient; // 对NettyClient的装饰，发送数据
    // key(host+port) value(ProtocolClient)
    // key(interfaceName-version)
    protected Map<String,List<ProtocolClient>> protocolClientMap=new ConcurrentHashMap<>();

    // 创建ExchangeServer
    protected void openServer(URL url) throws Throwable {
        if(protocolServer==null){
            protocolServer=createServer(url);
        }
    }

    protected void openClient(URL url) throws Throwable {
        log.info("准备新建netty连接：{}",url);
        ProtocolClient protocolClient=createClient(url);
        String clientKey=ProtocolUtils.serviceNodeKey(url);
        List<ProtocolClient> clientList = protocolClientMap.getOrDefault(clientKey, new ArrayList<>());
        clientList.add(protocolClient);
        protocolClientMap.put(clientKey,clientList);
    }

    protected abstract ProtocolServer createServer(URL url) throws Throwable;
    protected abstract ProtocolClient createClient(URL url) throws Throwable;
    protected static String serviceKey(URL url) {
        return ProtocolUtils.serviceKey(url);
    }
}
