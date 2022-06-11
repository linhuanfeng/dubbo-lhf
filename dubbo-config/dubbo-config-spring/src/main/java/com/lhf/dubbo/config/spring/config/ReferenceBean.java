package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.annotation.RpcReference;
import com.lhf.dubbo.registry.zookeeper.RegistryProtocol;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Map;

public class ReferenceBean<T> implements ApplicationContextAware {
    private RegistryProtocol registryProtocol;
    private RegistryConfig registryConfig;

    public ReferenceBean(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init();
        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            for (Field field : bean.getClass().getDeclaredFields()) {
                if(field.getAnnotation(RpcReference.class)!=null){
                    field.setAccessible(true);
                    try {
                        Object proxy = registryProtocol.refer(field.getType());
                        field.set(bean,proxy);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void init() {
        if(registryProtocol==null){
            registryProtocol=new RegistryProtocol(registryConfig);
        }
    }
}