package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.annotation.RpcReference;
import com.lhf.dubbo.common.bean.ServiceType;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.registry.zookeeper.RegistryProtocol;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

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
                        Object proxy = registryProtocol.refer(field.getType(),generateUrl(field));
                        field.set(bean,proxy);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private URL generateUrl(Field field){
        URL url = new URL();
        url.setInterfaceName(field.getDeclaredAnnotation(RpcReference.class).interfaceClass().getName());
        url.setType(ServiceType.provider);
        url.setVersion(field.getDeclaredAnnotation(RpcReference.class).version());
        return url;
    }

    private void init() {
        if(registryProtocol==null){
            registryProtocol=new RegistryProtocol(registryConfig);
        }
    }
}