package com.lhf.dubbo.remoting.zokeeper;

import java.util.List;

public interface ZookeeperClient {
    void create(String path, boolean ephemeral);
    void create(String path,String data, boolean ephemeral);
    List<String> getChildrenData(String path) throws Exception;
    void delete(String path);
}
