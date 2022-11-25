package com.lhf.dubbo.registry;


public interface RegistryFactory {
    Registry getRegistry(RegistryConfig registryConfig);
}
