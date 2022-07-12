package com.lhf.dubbo.common.bean.registry;

import com.lhf.dubbo.common.bean.URL;

import java.util.List;
import java.util.Map;

/**
 * 更新服务列表的回调函数
 */
@FunctionalInterface
public interface UpdateServerListCallBack {
    /**
     *
     * @param urlMap 对应的服务器列表
     */
    void latestUrl(Map<ServerChangeType,List<URL>> urlMap);
}
