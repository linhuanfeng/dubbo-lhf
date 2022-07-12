package com.example.servertest.impl;


import com.example.commonapi.api.EatService;
import com.lhf.dubbo.common.annotation.RpcService;

//@RpcService(interfaceClass = EatService.class,version = "1.0.0")
public class EatServiceImpl implements EatService {
    @Override
    public void eat(String name) {
        System.out.println(name+"可以吃饭了");
    }
}
