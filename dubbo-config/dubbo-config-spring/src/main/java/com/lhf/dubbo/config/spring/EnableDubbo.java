package com.lhf.dubbo.config.spring;

import com.lhf.dubbo.config.spring.config.DubboAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DubboAutoConfig.class})
public @interface EnableDubbo {
}
