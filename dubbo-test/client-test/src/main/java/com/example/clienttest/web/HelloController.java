package com.example.clienttest.web;

import com.example.commonapi.api.HelloService;
import com.lhf.dubbo.common.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @RpcReference(interfaceClass = HelloService.class,version = "1.0.0")
    private HelloService service;
    @GetMapping("/sayhello")
    public String sayHello(){
        return "远程调用的结果是："+service.sayHello("张三");
    }

}
