package com.lhf.dubbo.test.impl;

import com.lhf.dubbo.common.annotation.RpcService;
import com.lhf.dubbo.test.api.HelloService;

@RpcService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好:"+name;
    }
}
