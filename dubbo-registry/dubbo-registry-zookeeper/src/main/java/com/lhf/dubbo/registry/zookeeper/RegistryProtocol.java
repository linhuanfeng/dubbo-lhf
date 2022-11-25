package com.lhf.dubbo.registry.zookeeper;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.bean.registry.UpdateServerListCallBack;
import com.lhf.dubbo.registry.Registry;
import com.lhf.dubbo.registry.RegistryConfig;
import com.lhf.dubbo.rpc.Exporter;
import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.Protocol;
import com.lhf.dubbo.rpc.protocol.AbstractDubboProtocol;
import com.lhf.dubbo.rpc.protocol.dubbo.DubboProtocol;

import java.util.List;

/**
 * 注册中心对象，负责服务注册和暴露，调用DubboProtocol进行服务暴露
 */
public class RegistryProtocol implements Protocol {
    private AbstractDubboProtocol dubboProtocol; // 可共用
    private Registry registry;

    private RegistryConfig registryConfig;

    public RegistryProtocol(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    /**
     * 服务注册和暴露
     *
     * @param invoker 把服务封装成一个invoker
     * @param <T>
     * @return
     */
    public <T> Exporter<T> export(Invoker<T> invoker){
        // 服务注册的url
        URL registryUrl = invoker.getUrl();

        // 本地服务暴露，具体交给DubboProtocol暴露,并得到一个Exporter暴露对象
        Exporter exporter = null;
        try {
            exporter = doLocalExport(invoker);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        // 服务注册 ?为什么老是注册很久
        register(registryUrl);
        return exporter;
    }

    /**
     * 服务暴露
     * 调用DubboProtocol进行服务暴露（创建NettyServer监听端口），返回exporter暴露对象
     *
     * @param invoker
     * @param <T>
     * @return
     */
    private <T> Exporter doLocalExport(Invoker<T> invoker) throws Throwable {
        if (dubboProtocol == null) {
            dubboProtocol = new DubboProtocol();
        }
        return dubboProtocol.export(invoker);
    }

    /**
     * 服务注册
     *
     * @param registerProviderUrl
     */
    private void register(URL registerProviderUrl) {
        if (registry == null) {
            registry = getRegistry();
        }
        registry.register(registerProviderUrl);
    }

    /**
     * 根据URL判断返回对应协议的注册器
     * 这里通过工程获取ZookeeperRegistry实例
     *
     * @return
     */
    private Registry getRegistry() {
        return new ZookeeperRegistryFactory().getRegistry(registryConfig);
    }

    /**
     * 服务发现
     * @param url
     * @param updateServerListCallBack
     * @return
     */
    private List<URL> getProviderUrls(URL url, UpdateServerListCallBack updateServerListCallBack) {
        if (registry == null)
            registry = getRegistry();
        try {
            return registry.discovery(url,updateServerListCallBack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object refer(Class type, URL url) throws Throwable {
        if (dubboProtocol == null) {
            dubboProtocol = new DubboProtocol();
        }
        // 注册中心获取服务列表，传递回调对象，更新dubboProtocol的exporterMap
        List<URL> providerUrls = getProviderUrls(url,urlMap -> {
            dubboProtocol.updateProtocolClientMap(url,urlMap);
        });
        for (URL providerUrl : providerUrls) {
            providerUrl.setRetries(url.getRetries());  // 以注解的重试次数为准
            providerUrl.setHeartBeatConfig(url.getHeartBeatConfig());
        }
        // 返回远程代理对象
        return dubboProtocol.refer(type, providerUrls);
    }
}
