package com.example.servertest.impl;


import com.example.commonapi.api.HelloService;
import com.lhf.dubbo.common.annotation.RpcService;

@RpcService(interfaceClass = HelloService.class,version = "1.0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println(name);
        return "1111你好:"+name;
    }
}
