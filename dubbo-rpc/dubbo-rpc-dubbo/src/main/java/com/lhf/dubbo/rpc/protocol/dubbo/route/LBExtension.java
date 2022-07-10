package com.lhf.dubbo.rpc.protocol.dubbo.route;

import com.lhf.dubbo.rpc.protocol.dubbo.route.config.LBProperties;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class LBExtension {
    private static volatile Map<String,LoadBalance> lBMap;
    private static Object object=new Object();
    private static void init() {
        if (lBMap == null) {
            synchronized (object) {
                if (lBMap == null) {
                    lBMap = new ConcurrentHashMap<>();
                    // spi机制获取负载均衡器
                    ServiceLoader<LoadBalance> loadBalances = ServiceLoader.load(LoadBalance.class);
                    for (LoadBalance loadBalance : loadBalances) {
                        lBMap.put(loadBalance.getType().getName(), loadBalance);
                    }
                }
            }
        }
    }
    public static LoadBalance getLoadBalance(){
        init();
        return lBMap.get(LBProperties.getLbType().getName());
    }
}
