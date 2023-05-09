package com.example.servertest;


import com.example.commonapi.api.HelloService;
import com.lhf.dubbo.common.annotation.RpcService;

//@RpcService(interfaceClass = HelloService.class,version = "1.0.0")
public class HelloServiceImpl2 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "2222你好:"+name;
    }
}
