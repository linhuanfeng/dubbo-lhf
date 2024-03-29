package com.example.web;

import com.example.commonapi.api.HelloService;
import com.lhf.dubbo.common.annotation.RpcReference;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @DubboReference(interfaceClass = HelloService.class,version = "1.0.0",check = false)
    private HelloService service;

//    @RpcReference(interfaceClass = HelloService.class,version = "1.0.0")
//    private HelloService service2;

    @GetMapping("/sayhello")
    public String sayHello(){
        return "远程调用的结果是："+service.sayHello("张三");
    }

//    @GetMapping("/sayhello2")
//    public String sayHello2(){
//        return "远程调用的结果是："+service2.sayHello("张三");
//    }
}
