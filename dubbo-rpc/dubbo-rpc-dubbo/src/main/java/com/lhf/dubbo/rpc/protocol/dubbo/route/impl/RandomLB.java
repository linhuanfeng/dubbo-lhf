package com.lhf.dubbo.rpc.protocol.dubbo.route.impl;

import com.lhf.dubbo.rpc.ProtocolClient;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;
public class RandomLB implements LoadBalance {
    private Random random = new Random();
    @Override
    public LBEnum getType() {
        return LBEnum.randm;
    }

    @Override
    public ProtocolClient select(String serviceKey, List<ProtocolClient> urlList) {
        Assert.notEmpty(urlList,"urlList is null,serviceKey:"+serviceKey);
        return urlList.get(random.nextInt(urlList.size()));
    }
}
