package com.lhf.dubbo.registry.zookeeper;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.bean.registry.UpdateServerListCallBack;
import com.lhf.dubbo.common.utils.JsonUtil;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.registry.Registry;
import com.lhf.dubbo.remoting.zokeeper.ZookeeperClient;
import com.lhf.dubbo.remoting.zookeeper.curator.CuratorZookeeperClient;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * zk注册中心
 */
public class ZookeeperRegistry implements Registry {
    private ZookeeperClient zkClient;

    public ZookeeperRegistry(RegistryConfig registryConfig) {
        this.zkClient=new CuratorZookeeperClient(registryConfig);
    }

    // 服务注册
    @Override
    public void register(URL url){
        zkClient.create(ProtocolUtils.serviceKey(url), JsonUtil.ObjectToJson(url),true);
    }
    /**
     * 服务发现
     * 那么如何服务动态更新呢
      */
    @Override
    public List<URL> discovery(URL url, UpdateServerListCallBack updateServerListCallBack) throws Exception {
//        List<String> services = zkClient.getChildrenData(ProtocolUtils.serviceKeyParent(url));
        List<String> services = zkClient.getChildrenDataCache(ProtocolUtils.serviceKeyParent(url),updateServerListCallBack);
        return services.stream().map(s->JsonUtil.JsonToObject(s,URL.class)).collect(Collectors.toList());
    }

    @Override
    public void unRegister(URL url) {

    }
}
