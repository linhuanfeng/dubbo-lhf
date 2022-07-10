package com.lhf.dubbo.rpc.protocol.dubbo.route.impl;

import com.lhf.dubbo.rpc.ProtocolClient;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 基于linkedHashMap，但实际效果和轮询一致
@Deprecated
public class LRULB implements LoadBalance {
    // key(interfaceName+version) value(key(ProtocolClient),value(ProtocolClient))
    private Map<String, LinkedHashMap<ProtocolClient, ProtocolClient>> serviceMap=new ConcurrentHashMap<>();
    @Override
    public LBEnum getType() {
        return LBEnum.LRU;
    }

    @Override
    public ProtocolClient select(String serviceKey,List<ProtocolClient> urlList) {
        LinkedHashMap<ProtocolClient, ProtocolClient> lruMap = serviceMap.get(serviceKey);
        if(lruMap==null){
            // 服务第一次访问进行初始化
            lruMap = new LinkedHashMap<>(
                    100,0.75f,true);
            for (ProtocolClient client : urlList) {
                lruMap.put(client,client);
            }
            serviceMap.put(serviceKey,lruMap);
        }
        //TODO 移除没用的服务提供者
        Map.Entry<ProtocolClient, ProtocolClient> oldest = lruMap.entrySet().iterator().next();
        return lruMap.get(oldest.getKey());
    }
}
