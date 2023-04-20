package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "hello-service")
public interface HelloService {
    @GetMapping("/hello/sayhello")
    String sayHello();
}
