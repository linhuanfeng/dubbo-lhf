package com.lhf.dubbo.registry;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.bean.registry.UpdateServerListCallBack;

import java.util.List;

/**
 * 服务注册和发现
 */
public interface Registry {
    void register(URL url);
    void unRegister(URL url);
    List<URL> discovery(URL url, UpdateServerListCallBack updateServerListCallBack) throws Exception;
}
