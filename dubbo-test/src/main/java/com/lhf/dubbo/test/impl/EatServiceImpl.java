package com.lhf.dubbo.test.impl;

import com.lhf.dubbo.common.annotation.RpcService;
import com.lhf.dubbo.test.api.EatService;

@RpcService(interfaceClass = EatService.class,version = "1.0.0")
public class EatServiceImpl implements EatService {
    @Override
    public void eat(String name) {
        System.out.println(name+"可以吃饭了");
    }
}
