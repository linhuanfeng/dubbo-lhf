package com.lhf.dubbo.registry.zookeeper.zokeeper;

import com.lhf.dubbo.common.bean.registry.UpdateServerListCallBack;

import java.util.List;

public interface ZookeeperClient {
    void create(String path, boolean ephemeral);
    void create(String path,String data, boolean ephemeral);
    List<String> getChildrenData(String path) throws Exception;
    List<String> getChildrenDataCache(String path, UpdateServerListCallBack updateServerListCallBack) throws Exception;
    void delete(String path);
}
