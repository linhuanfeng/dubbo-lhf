package com.lhf.dubbo.config.spring;

import com.lhf.dubbo.config.spring.config.DubboAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启框架自动装配
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DubboAutoConfig.class})
public @interface EnableDubbo {
}
