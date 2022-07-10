package com.lhf.dubbo.test.impl;

import com.lhf.dubbo.common.annotation.RpcService;
import com.lhf.dubbo.test.api.HelloService;

@RpcService(interfaceClass = HelloService.class,version = "1.0.0")
public class HelloServiceImpl2 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "你好(v1.0.1):"+name;
    }
}
