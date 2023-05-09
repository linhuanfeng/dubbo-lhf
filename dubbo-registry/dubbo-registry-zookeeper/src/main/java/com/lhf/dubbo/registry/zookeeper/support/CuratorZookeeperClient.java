package com.lhf.dubbo.registry.zookeeper.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.bean.registry.ServerChangeType;
import com.lhf.dubbo.common.bean.registry.UpdateServerListCallBack;
import com.lhf.dubbo.common.utils.JsonUtil;
import com.lhf.dubbo.registry.RegistryConfig;
import com.lhf.dubbo.registry.zookeeper.zokeeper.AbstractZookeeperClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CuratorZookeeperClient extends AbstractZookeeperClient {
    private final CuratorFramework client;
    private RegistryConfig registryConfig;

    public CuratorZookeeperClient() {
        this(new RegistryConfig());
    }

    public CuratorZookeeperClient(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(3000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getConnectString())
                .connectionTimeoutMs(registryConfig.getSessionTimeout())
                .retryPolicy(retryPolicy).build();
        client.start();
    }

    // 根据url进行服务发现,path:/root/interfaceName/version/provider
    @Override
    public List<String> getChildrenData(String path) throws Exception {
        List<String> list = client.getChildren().forPath(path); // 可用的服务端结点
        List<String> serverInfoList = new ArrayList<>(list.size());
        for (String s : list) {
            serverInfoList.add(new String(client.getData().forPath(path + "/" + s))); // 可用结点的data信息
        }
        return serverInfoList;
    }

    @Override
    public List<String> getChildrenDataCache(String path, UpdateServerListCallBack updateServerListCallBack) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true); //true表示结点数据缓存起来
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE); // 同步的方式初始化
//        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT); // 异步且有事件
        List<ChildData> childDataList = pathChildrenCache.getCurrentData(); // 孩子结点数据列表

        // 结点监听
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                handlerEvent(event, path, updateServerListCallBack);
            }
        });

        List<String> serverInfoList = childDataList.stream().map(cd -> new String(cd.getData())).collect(Collectors.toList());
        return serverInfoList;
    }

    private void handlerEvent(PathChildrenCacheEvent event, String path, UpdateServerListCallBack updateServerListCallBack) {
        Map<ServerChangeType, List<URL>> map = new HashMap<>();
        List<URL> urlList = new ArrayList<>();
        if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
            log.info("子节点新增:path:{},data:{}", event.getData().getPath(), new String(event.getData().getData()));
            urlList.add(JsonUtil.JsonToObject(new String(event.getData().getData()), URL.class));
            map.put(ServerChangeType.SERVER_ADD, urlList);
        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
            log.info("子节点移除:path:{},data:{}", event.getData().getPath(), new String(event.getData().getData()));
            urlList.add(JsonUtil.JsonToObject(new String(event.getData().getData()), URL.class));
            map.put(ServerChangeType.SERVER_REMOVE, urlList);
        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
            log.info("子节点数据更新:path:{},data:{}", event.getData().getPath(), new String(event.getData().getData()));
            urlList.add(JsonUtil.JsonToObject(new String(event.getData().getData()), URL.class));
            map.put(ServerChangeType.SERVER_UPDATE, urlList);
        } else {
            log.info("事件:{},不需要更新服务结点", event);
            return; // 初始时event的data字段为null
        }
        updateServerListCallBack.latestUrl(map);
    }

    @Override
    public void create(String path, boolean ephemeral) {
        super.create(path, ephemeral);
    }

    /**
     * 创建临时带序号的结点
     * @param path
     */
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
