package com.example.impl;

import com.example.commonapi.api.EatService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = EatService.class,version = "1.0.0")
public class EatServiceImpl implements EatService {
    @Override
    public void eat(String name) {
        System.out.println(name+"可以吃饭了");
    }
}
