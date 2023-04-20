package com.example.web;

import com.example.feign.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Resource
    private HelloService service;

//    @RpcReference(interfaceClass = HelloService.class,version = "1.0.0")
//    private HelloService service2;

    @GetMapping("/sayhello")
    public String sayHello(){
        return "远程调用的结果是："+service.sayHello();
    }

//    @GetMapping("/sayhello2")
//    public String sayHello2(){
//        return "远程调用的结果是："+service2.sayHello("张三");
//    }
}
