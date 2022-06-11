package com.lhf.dubbo.test.web;

import com.lhf.dubbo.common.annotation.RpcReference;
import com.lhf.dubbo.test.api.EatService;
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
