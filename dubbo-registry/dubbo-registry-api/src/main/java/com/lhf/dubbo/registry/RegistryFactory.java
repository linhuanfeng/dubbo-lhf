package com.lhf.dubbo.registry;

import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;

public interface RegistryFactory {
    Registry getRegistry(RegistryConfig registryConfig);
}
