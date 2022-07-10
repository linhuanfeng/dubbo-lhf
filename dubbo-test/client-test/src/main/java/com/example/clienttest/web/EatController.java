package com.example.clienttest.web;

import com.example.commonapi.api.EatService;
import com.lhf.dubbo.common.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eat")
public class EatController {
    @RpcReference(interfaceClass = EatService.class, version = "1.0.0")
    private EatService eatService;

    @GetMapping("/eat")
    public void eat() {
        eatService.eat("李四");
    }
}
