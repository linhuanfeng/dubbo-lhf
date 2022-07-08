package com.lhf.dubbo.test.web;

import com.lhf.dubbo.common.annotation.RpcReference;
import com.lhf.dubbo.test.api.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @RpcReference(interfaceClass = HelloService.class,version = "1.0.0")
    private HelloService service;

    @RpcReference(interfaceClass = HelloService.class,version = "1.0.1")
    private HelloService service2;

    @GetMapping("/sayhello")
    public String sayHello(){
        return "远程调用的结果是："+service.sayHello("张三");
    }

    @GetMapping("/sayhello2")
    public String sayHello2(){
        return "远程调用的结果是："+service2.sayHello("张三");
    }
}
