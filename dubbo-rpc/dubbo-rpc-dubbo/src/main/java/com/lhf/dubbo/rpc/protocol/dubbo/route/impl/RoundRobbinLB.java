package com.lhf.dubbo.rpc.protocol.dubbo.route.impl;

import com.lhf.dubbo.rpc.ProtocolClient;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobbinLB implements LoadBalance {
    private AtomicInteger roundRobin=new AtomicInteger(0);

    public LBEnum getType() {
        return LBEnum.robbin;
    }

    @Override
    public ProtocolClient select(String serviceKey, List<ProtocolClient> urlList) {
        return urlList.get(roundRobin.getAndIncrement()% urlList.size());
    }
}
