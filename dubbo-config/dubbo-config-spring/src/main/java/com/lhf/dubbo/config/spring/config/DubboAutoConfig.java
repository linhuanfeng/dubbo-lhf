package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import com.lhf.dubbo.rpc.protocol.dubbo.config.ProtocolConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({RegistryConfig.class, ProtocolConfig.class,ReferenceConfig.class,ServiceConfig.class})
public class DubboAutoConfig {
    @Resource
    private RegistryConfig registryConfig;

    @Resource
    private ReferenceConfig referenceConfig;

    @Resource
    private ServiceConfig serviceConfig;

    @Resource
    private ProtocolConfig protocolConfig;

    @Bean
    public ServiceBean serviceBean(){
        return new ServiceBean(registryConfig,protocolConfig,serviceConfig);
    }

    @Bean
    public ReferenceBean referenceBean(){
        return new ReferenceBean(registryConfig,referenceConfig);
    }
}
