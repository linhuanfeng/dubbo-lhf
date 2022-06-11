package com.lhf.dubbo.test;

import com.lhf.dubbo.config.spring.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DubboTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboTestApplication.class,args);
    }
}
