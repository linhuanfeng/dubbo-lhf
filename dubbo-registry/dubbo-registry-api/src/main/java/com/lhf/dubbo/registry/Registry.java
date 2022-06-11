package com.lhf.dubbo.registry;

import com.lhf.dubbo.common.bean.URL;

import java.util.List;

/**
 * 服务注册和发现
 */
public interface Registry {
    void register(URL url);
    void unRegister(URL url);
    List<URL> discovery(URL url) throws Exception;
}
