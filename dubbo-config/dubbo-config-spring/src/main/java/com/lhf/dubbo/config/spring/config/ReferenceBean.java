package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.annotation.RpcReference;
import com.lhf.dubbo.common.bean.ServiceType;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.config.HeartBeatConfig;
import com.lhf.dubbo.registry.zookeeper.RegistryProtocol;
import com.lhf.dubbo.registry.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

public class ReferenceBean<T> implements ApplicationContextAware, SmartInitializingSingleton {
    private RegistryProtocol registryProtocol;
    private RegistryConfig registryConfig;
    private ReferenceConfig referenceConfig;

    private ApplicationContext applicationContext;

    public ReferenceBean(RegistryConfig registryConfig,ReferenceConfig referenceConfig) {
        this.registryConfig = registryConfig;
        this.referenceConfig=referenceConfig;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        registryProtocol=new RegistryProtocol(registryConfig);
        this.applicationContext=applicationContext;
    }

    private URL generateUrl(Field field){
        RpcReference rpcReference = field.getDeclaredAnnotation(RpcReference.class);
        URL url = new URL();
        url.setInterfaceName(rpcReference.interfaceClass().getName());
        url.setType(ServiceType.provider);
        url.setVersion(rpcReference.version());
        url.setRetries(referenceConfig.getRetries());
        HeartBeatConfig beatConfig = new HeartBeatConfig();
        beatConfig.setBEAT_INTERVAL(referenceConfig.getBeatInternal());
//        beatConfig.setBEAT_INTERVAL(referenceConfig.getBeatTimeout());
//        if(rpcReference.beatInternal()!=-1){
//            // 以注解的为准
//            beatConfig.setBEAT_INTERVAL(rpcReference.beatInternal());
//        }
        url.setHeartBeatConfig(beatConfig);
        return url;
    }

    /**
     * 如何创建代理对象：
     * 从容器中拿对象，如果有字段标注了该属性，利用反射进行属性注入
     */
    @Override
    public void afterSingletonsInstantiated() {
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
}