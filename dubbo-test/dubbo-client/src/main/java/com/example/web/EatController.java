package com.example.web;

import com.example.commonapi.api.EatService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/eat")
public class EatController {
    @DubboReference(interfaceClass = EatService.class, version = "1.0.0",check = false)
    private EatService eatService;

    @GetMapping("/eat")
    public void eat() {
        eatService.eat("李四");
    }
}
