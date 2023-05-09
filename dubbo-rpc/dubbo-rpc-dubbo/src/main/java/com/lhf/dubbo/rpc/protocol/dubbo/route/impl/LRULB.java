package com.lhf.dubbo.rpc.protocol.dubbo.route.impl;

import com.lhf.dubbo.rpc.ProtocolClient;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// 基于linkedHashMap，但实际效果和轮询一致
//@Deprecated
public class LRULB implements LoadBalance {
    // key(interfaceName+version) value(key(ProtocolClient),value(ProtocolClient))
    private Map<String, LinkedHashMap<ProtocolClient, ProtocolClient>> serviceMap=new ConcurrentHashMap<>();
    @Override
    public LBEnum getType() {
        return LBEnum.LRU;
    }

    @Override
    public ProtocolClient select(String serviceKey,List<ProtocolClient> urlList) {
        Assert.notEmpty(urlList,"urlList is null,serviceKey:"+serviceKey);
        LinkedHashMap<ProtocolClient, ProtocolClient> lruMap = serviceMap.get(serviceKey);
        if(lruMap==null||lruMap.size()!=urlList.size()){
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
