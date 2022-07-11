package com.lhf.dubbo.registry.zookeeper;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.registry.Registry;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import com.lhf.dubbo.rpc.Exporter;
import com.lhf.dubbo.rpc.Invoker;
import com.lhf.dubbo.rpc.Protocol;
import com.lhf.dubbo.rpc.protocol.dubbo.DubboProtocol;

import java.util.List;

/**
 * 注册中心对象，负责服务注册和暴露，调用DubboProtocol进行服务暴露
 */
public class RegistryProtocol implements Protocol {
    private Protocol dubboProtocol; // 可共用
    private Registry registry;

    private RegistryConfig registryConfig;

    public RegistryProtocol(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    /**
     * 服务注册和暴露
     * @param invoker 把服务封装成一个invoker
     * @param <T>
     * @return
     */
    public <T> Exporter<T> export(Invoker<T> invoker) throws Throwable {
        // 服务注册的url
        URL registryUrl=invoker.getUrl();

        // 本地服务暴露，具体交给DubboProtocol暴露,并得到一个Exporter暴露对象
        Exporter exporter=doLocalExport(invoker);

        // 服务注册 ?为什么老是注册很久
        register(registryUrl);
        return exporter;
    }

    /**
     * 服务暴露
     * 调用DubboProtocol进行服务暴露（创建NettyServer监听端口），返回exporter暴露对象
     * @param invoker
     * @param <T>
     * @return
     */
    private <T> Exporter doLocalExport(Invoker<T> invoker) throws Throwable {
        if(dubboProtocol==null){
            dubboProtocol = new DubboProtocol();
        }
        return dubboProtocol.export(invoker);
    }

    /**
     * 服务注册
     * @param registerProviderUrl
     */
    private void register(URL registerProviderUrl){
        if(registry==null){
            registry=getRegistry();
        }
        registry.register(registerProviderUrl);
    }

    /**
     * 服务发现
     * @return
     * @param <T>
     */
    private <T> URL getProviderUrl(URL url) {
        List<URL> providerUrls = getProviderUrls(url);
        // 负载均衡
        return providerUrls.get(0);
    }

    private List<URL> getProviderUrls(URL url){
        if(registry==null)
            registry=getRegistry();
        try {
            return registry.discovery(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据URL判断返回对应协议的注册器
     * 这里通过工程获取ZookeeperRegistry实例
     * @return
     */
    private Registry getRegistry() {
        return new ZookeeperRegistryFactory().getRegistry(registryConfig);
    }

    @Override
    public Object refer(Class type, URL url) throws Throwable{
        List<URL> providerUrls = getProviderUrls(url);
        for (URL providerUrl : providerUrls) {
            providerUrl.setRetries(url.getRetries());  // 以注解的重试次数为准
            providerUrl.setHeartBeatConfig(url.getHeartBeatConfig());
        }
        if(dubboProtocol==null){
            dubboProtocol = new DubboProtocol();
        }
        return dubboProtocol.refer(type,providerUrls);
    }
}
