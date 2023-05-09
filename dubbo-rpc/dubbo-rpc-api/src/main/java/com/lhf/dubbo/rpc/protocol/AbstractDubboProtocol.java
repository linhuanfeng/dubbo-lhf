package com.lhf.dubbo.rpc.protocol;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.bean.registry.ServerChangeType;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class AbstractDubboProtocol implements Protocol {

    /**
     * key(interfaceName-version) value(Map(clientId,ProtocolClient))
     * 服务标识key，对应的所有实例(channelId,ProtocolClient)
     */
    protected Map<String, Map<String, ProtocolClient>> protocolClientMap = new ConcurrentHashMap<>();
    private AtomicBoolean serverStarted = new AtomicBoolean();
    // 单线程的线程池，负责更新服务列表，无并发问题
    private ExecutorService executorService = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * 更新客户端列表
     *
     * @param oldUrl 老客户端的URL信息，比如重试次数和心跳间隔，urlMap是服务端的url，没有这些信息的
     * @param urlMap
     */
    public void updateProtocolClientMap(URL oldUrl, Map<ServerChangeType, List<URL>> urlMap) {
//            Map<String,Map<String,ProtocolClient>> newMap=new ConcurrentHashMap<>(protocolClientMap);
        for (Map.Entry<ServerChangeType, List<URL>> entry : urlMap.entrySet()) {
            if (entry.getKey().equals(ServerChangeType.SERVER_REMOVE)) {
                // 关闭服务结点
                for (URL url : entry.getValue()) {
                    log.info("删除服务结点：{}", url);
                    populateNewClientUrl(url, oldUrl);
                    closeClient(url);
                }
            } else {
//                 if (entry.getKey().equals(ServerChangeType.SERVER_ADD)) {
                // 新增服务结点
                for (URL url : entry.getValue()) {
                    try {
                        log.info("更新服务结点：{}", url);
                        populateNewClientUrl(url, oldUrl);
                        openClient(url);
                    } catch (Throwable e) {
                        log.error("新增服务结点异常：{}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
//                 }
            }
        }
//        executorService.submit();
    }

    protected void populateNewClientUrl(URL newUrl, URL oldUrl) {
        newUrl.setRetries(oldUrl.getRetries());
        newUrl.getHeartBeatConfig().setBEAT_INTERVAL(oldUrl.getHeartBeatConfig().getBEAT_INTERVAL());
    }

    protected final void openServer(URL url) throws Throwable {
        // 模板方法，保证本地只启动一个服务器
        if (serverStarted.compareAndSet(false, true)) {
            createServer(url);
        }
    }

    protected void closeClient(URL url) {
        // 不用关闭，会自动重连并关闭
    }

    protected void openClient(URL url) throws Throwable {
        log.info("创建netty连接中：{}", url.getInterfaceName() + "-" + url.getVersion() + "-" + url.getPort());
        // 创建netty连接(客户端)
        ProtocolClient protocolClient = createClient(url);
        // 获取服务标识key
        String serviceNodeKey = ProtocolUtils.serviceNodeKey(url); // interfaceName-version
        // 得到服务Key对应所有实例
        Map<String, ProtocolClient> clientMap = protocolClientMap.getOrDefault(serviceNodeKey, new ConcurrentHashMap<>());
        // 添加新实例
        clientMap.put(protocolClient.getClient().getId(), protocolClient);
        protocolClientMap.put(serviceNodeKey, clientMap);
    }

    protected abstract ProtocolServer createServer(URL url) throws Throwable;

    protected abstract ProtocolClient createClient(URL url) throws Throwable;
}
