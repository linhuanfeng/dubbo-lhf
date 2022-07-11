package com.lhf.dubbo.remoting.zookeeper.curator;

import com.lhf.dubbo.remoting.zokeeper.AbstractZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;
public class CuratorZookeeperClient extends AbstractZookeeperClient {
    private final CuratorFramework client;
    private RegistryConfig registryConfig;

    public CuratorZookeeperClient(){
        this(new RegistryConfig());
    }

    public CuratorZookeeperClient(RegistryConfig registryConfig){
        this.registryConfig=registryConfig;
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(3000,3);
        client= CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getConnectString())
                .connectionTimeoutMs(registryConfig.getSessionTimeout())
                .retryPolicy(retryPolicy).build();
        client.start();
    }

    // 根据url进行服务发现,path:/root/interfaceName/version/provider
    public List<String> getChildrenData(String path) throws Exception {
        List<String> list = client.getChildren().forPath(path); // 可用的服务端结点
        List<String> serverInfoList=new ArrayList<>(list.size());
        for (String s : list) {
            serverInfoList.add(new String(client.getData().forPath(path + "/" + s))); // 可用结点的data信息
        }
        return serverInfoList;
    }

    @Override
    public void create(String path, boolean ephemeral) {
        super.create(path, ephemeral);
    }

    protected void createEphemeral(String path) {
        try {
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void createPersistent(String path) {

    }

    protected void createEphemeral(String path, String data) {
        byte[] dataBytes = data.getBytes(registryConfig.getCHARSET());
        try {
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path, dataBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void createPersistent(String path, String data) {

    }
}
