package com.lhf.dubbo.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.util.concurrent.TimeUnit;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {
    Class<?> interfaceClass() default void.class;
    String version() default "";

//    /**
//     * 调用超时时间，单位TimeUnit.SECONDS
//     * @return
//     */
//    int timeout() default -1;
//
//    /**
//     * 重试次数
//     * @return
//     */
//    int retries() default -1;
//
//    /**
//     * 客户端发送心跳时间
//     * @return
//     */
//    int beatInternal() default -1;
}
