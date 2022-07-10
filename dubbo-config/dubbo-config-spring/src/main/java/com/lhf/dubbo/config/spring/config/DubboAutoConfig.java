package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import com.lhf.dubbo.rpc.protocol.dubbo.ProtocolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({RegistryConfig.class, ProtocolConfig.class})
public class DubboAutoConfig {
    @Resource
    private RegistryConfig registryConfig;

    @Resource
    private ProtocolConfig protocolConfig;

    @Bean
    public ServiceBean serviceBean(){
        return new ServiceBean(registryConfig,protocolConfig);
    }

    @Bean
//    @DependsOn("serviceBean")
//    @Lazy // 表示懒加载
    public ReferenceBean referenceBean(){
        return new ReferenceBean(registryConfig);
    }
}
