package com.lhf.dubbo.registry.zookeeper;

import com.lhf.dubbo.registry.Registry;
import com.lhf.dubbo.registry.RegistryFactory;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;

public class ZookeeperRegistryFactory implements RegistryFactory {


    @Override
    public Registry getRegistry(RegistryConfig registryConfig) {
        ZookeeperRegistry registry = new ZookeeperRegistry(registryConfig);
        return registry;
    }
}
