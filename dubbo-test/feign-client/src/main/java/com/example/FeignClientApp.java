package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.example.feign")  // 扫描@FeignClient和@Configuration
@EnableDiscoveryClient
@SpringBootApplication
public class FeignClientApp {
    public static void main(String[] args) {
        SpringApplication.run(FeignClientApp.class,args);
    }
}
