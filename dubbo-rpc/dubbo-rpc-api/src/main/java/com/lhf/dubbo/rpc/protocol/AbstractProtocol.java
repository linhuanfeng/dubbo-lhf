package com.lhf.dubbo.rpc.protocol;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractProtocol implements Protocol {

    // key(interfaceName-version) value(Map(clientId,ProtocolClient))
    protected Map<String,Map<String,ProtocolClient>> protocolClientMap=new ConcurrentHashMap<>();
    private AtomicBoolean serverStarted=new AtomicBoolean();

    protected final void openServer(URL url) throws Throwable {
        // 模板方法，保证本地只启动一个服务器
        if(serverStarted.compareAndSet(false,true)){
            createServer(url);
        }
    }

    protected void openClient(URL url) throws Throwable {
        log.info("创建netty连接中：{}",url.getInterfaceName()+"-"+url.getVersion()+"-"+url.getPort());
        ProtocolClient protocolClient=createClient(url);
        String clientKey=ProtocolUtils.serviceNodeKey(url);
        Map<String, ProtocolClient> clientMap = protocolClientMap.getOrDefault(clientKey, new ConcurrentHashMap<>());
        clientMap.put(protocolClient.getClient().getId(),protocolClient);
        protocolClientMap.put(clientKey,clientMap);
    }

    protected abstract ProtocolServer createServer(URL url) throws Throwable;
    protected abstract ProtocolClient createClient(URL url) throws Throwable;
}
