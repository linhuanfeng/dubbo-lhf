package com.lhf.dubbo.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    Class<?> interfaceClass() default void.class;
    String version() default "";
//    /**
//     * 客户端发送心跳时间
//     * @return
//     */
//    int beatInternal() default -1;
//    /**
//     * 心跳超时时间，单位TimeUnit.SECONDS
//     * @return
//     */
//    int beatTimeout() default -1;
}
