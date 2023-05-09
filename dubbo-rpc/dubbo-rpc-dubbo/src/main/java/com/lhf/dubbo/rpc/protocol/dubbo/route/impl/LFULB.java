package com.lhf.dubbo.rpc.protocol.dubbo.route.impl;

import com.lhf.dubbo.rpc.ProtocolClient;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;

import java.util.*;

/**
 * LFU load balance
 * Created by luxiaoxun on 2020-08-01.
 */
public class LFULB implements LoadBalance {
//    private ConcurrentMap<String, HashMap<RpcProtocol, Integer>> jobLfuMap = new ConcurrentHashMap<String, HashMap<RpcProtocol, Integer>>();
//    private long CACHE_VALID_TIME = 0;
//
//    public RpcProtocol doRoute(String serviceKey, List<RpcProtocol> addressList) {
//        // cache clear
//        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
//            jobLfuMap.clear();
//            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
//        }
//
//        // lfu item init
//        HashMap<RpcProtocol, Integer> lfuItemMap = jobLfuMap.get(serviceKey);
//        if (lfuItemMap == null) {
//            lfuItemMap = new HashMap<RpcProtocol, Integer>();
//            jobLfuMap.putIfAbsent(serviceKey, lfuItemMap);   // 避免重复覆盖
//        }
//
//        // put new
//        for (RpcProtocol address : addressList) {
//            if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1000000) {
//                lfuItemMap.put(address, 0);
//            }
//        }
//
//        // remove old
//        List<RpcProtocol> delKeys = new ArrayList<>();
//        for (RpcProtocol existKey : lfuItemMap.keySet()) {
//            if (!addressList.contains(existKey)) {
//                delKeys.add(existKey);
//            }
//        }
//        if (delKeys.size() > 0) {
//            for (RpcProtocol delKey : delKeys) {
//                lfuItemMap.remove(delKey);
//            }
//        }
//
//        // load least used count address
//        List<Map.Entry<RpcProtocol, Integer>> lfuItemList = new ArrayList<Map.Entry<RpcProtocol, Integer>>(lfuItemMap.entrySet());
//        Collections.sort(lfuItemList, new Comparator<Map.Entry<RpcProtocol, Integer>>() {
//            @Override
//            public int compare(Map.Entry<RpcProtocol, Integer> o1, Map.Entry<RpcProtocol, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
//
//        Map.Entry<RpcProtocol, Integer> addressItem = lfuItemList.get(0);
//        RpcProtocol minAddress = addressItem.getKey();
//        addressItem.setValue(addressItem.getValue() + 1);
//
//        return minAddress;
//    }
//
//    @Override
//    public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
//        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
//        List<RpcProtocol> addressList = serviceMap.get(serviceKey);
//        if (addressList != null && addressList.size() > 0) {
//            return doRoute(serviceKey, addressList);
//        } else {
//            throw new Exception("Can not find connection for service: " + serviceKey);
//        }
//    }

    @Override
    public LBEnum getType() {
        return LBEnum.lfu;
    }

    @Override
    public ProtocolClient select(String serviceKey, List<ProtocolClient> urlList) {
        return null;
    }
}
