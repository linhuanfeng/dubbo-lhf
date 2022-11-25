package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.common.bean.*;
import com.lhf.dubbo.common.config.HeartBeatConfig;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.remoting.*;
import com.lhf.dubbo.remoting.transport.netty.retry.ExponentialBackOffRetry;
import com.lhf.dubbo.remoting.transport.netty.retry.RetryPolicy;
import com.lhf.dubbo.remoting.transport.netty.support.Transporters;
import com.lhf.dubbo.rpc.*;
import com.lhf.dubbo.rpc.protocol.AbstractDubboProtocol;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBExtension;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DubboProtocol extends AbstractDubboProtocol {
    // key(interfaceName+version) val(Exporter)
    private Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();
    // key(requestId) val(RpcFuture)
    private Map<String, RpcFuture> pendingRpc = new ConcurrentHashMap<>();
    // 统一使用spi方式获取负载均衡器
    private LoadBalance loadBalance = LBExtension.getLoadBalance();
    // key(URL.id) value(RetryPolicy)
    private Map<String, RetryPolicy> retryPolicyMap = new ConcurrentHashMap<>();

    /**
     * 负责读写请求
     * @return
     */
    private ChannelHandler getChannelHandler() {
        return new AbstractChannelHandler() {
            @Override
            public void disconnected(Channel channel) {
                // 移除无效的client
                removeInvalidProtocolClient(channel);
                // 短线重连
                channelReconnect(channel);
            }

            @Override
            public void received(Channel channel, Object message) {
                Object result = reply(channel, message); // 处理消息
                if (result != null) {
                    channel.sent(result); // 响应消息
                }
            }

            public Object reply(Channel channel, Object message) {
                if (message instanceof RpcResponse) {
                    RpcResponse rpcResponse = (RpcResponse) message;
//                logger.debug("收到响应："+ rpcResponse);
                    RpcFuture rpcFuture = pendingRpc.get(rpcResponse.getRequestId());
                    if (rpcFuture != null) {
                        pendingRpc.remove(rpcResponse.getRequestId());
                        rpcFuture.setRpcResponse(rpcResponse);
                    } else {
//                    logger.debug("响应匹配失败："+ rpcResponse);
                    }
                } else if (message instanceof RpcRequest) {
                    RpcRequest request = (RpcRequest) message;
                    if (HeartBeatConfig.BEAT_ID.equals(request.getRequestId())) {
//                    log.info("channel:{},收到心跳，无需恢复",channel);
                        return null;
                    }
//                logger.debug("收到请求："+ request);
                    Invoker<?> invoker = getInvoker(request);
                    Object res = invoker.invoke(request);
                    RpcResponse response = new RpcResponse();
                    response.setRequestId(request.getRequestId());
                    response.setResult(res);
                    return response;
                } else {
//                logger.debug("收到普通消息："+ message);
                }
                return null;
            }
        };
    }

    /**
     * 利用channel的生命周期函数
     * 实现短线自动重连,多个ChannelHandler公用一个
     *
     */
    private void channelReconnect(Channel channel) {
        try {
            URL url = channel.getUrl();
            RetryPolicy retryPolicy = retryPolicyMap.get(url.getId());
            if (retryPolicy == null) {
                retryPolicy = new ExponentialBackOffRetry(url.getRetries());
                retryPolicyMap.put(url.getId(), retryPolicy);
            }
            if (retryPolicy.retry()) {
                log.info("短线重连,第{}次尝试,service:{},channel:{}",
                        retryPolicy.getCurRetries(),url.getInterfaceName() + "-" + url.getVersion(),
                        channel.getChannel().remoteAddress());
                openClient(url);
            } else {
                log.error("重连失败，curRetries:{} 达到 maxRetries:{}", retryPolicy.getCurRetries(), url.getRetries());
            }
        }catch (ConnectException e){
            log.error("重连失败，远程服务器可能宕机或拒绝连接：{}",e.getMessage());
        }catch (IOException e){
            log.error(e.getMessage());
        }
        catch (Throwable e) {
            log.error("重连失败，未知异常：{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void removeInvalidProtocolClient(Channel channel) {
        URL url = channel.getUrl();
        // 移除旧数据
        String serviceNodeKey = ProtocolUtils.serviceNodeKey(url);
        Map<String, ProtocolClient> clientMap = protocolClientMap.get(serviceNodeKey);
        if (clientMap == null) {
            throw new ConcurrentModificationException("当前客户端被别人移除了");
        }
        clientMap.remove(channel.getId());
    }

    Invoker<?> getInvoker(RpcRequest request) {
        String serviceName = request.getInterfaceName();
        String version = request.getVersion();
        Assert.hasText(serviceName, "serviceName不能为空,at " + request);
        Assert.hasText(version, "version不能为空,at " + request);
        Exporter<?> exporter = exporterMap.get(ProtocolUtils.exporterKey(serviceName, version));
        return exporter.getInvoker();
    }

    /**
     * 服务本地暴露，返回exporter暴露对象,并保存到exporterMap中。
     * 当接收服务调用的时候，根据serviceKey获取对于的exporter,再调用exporter::getInvoker得到Invoker
     * Invoker::invoke(..)得到结果
     * 启动netty服务器监听端口
     *
     * @param invoker
     * @param <T>
     * @return
     */
    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws Throwable {
        URL url = invoker.getUrl();
        // 开启netty服务,如果有多个服务，创建一个服务器即可
        openServer(url);
        // 创建暴露对象
        Exporter<T> exporter = new DubboExporter<>(invoker);
        // 缓存暴露对象，key(interfaceName:version) value(expoter)
        exporterMap.put(ProtocolUtils.exporterKey(invoker.getUrl().getInterfaceName(), invoker.getUrl().getVersion()), exporter);

        return exporter;
    }

    /**
     * 生成客户端代理对象
     * 有多个protocolClient，并实现负载均衡
     *
     * @param type
     * @param urls 所有可用的服务url
     * @return
     * @throws Throwable
     */
    @Override
    public Object refer(Class type, List<URL> urls) throws Throwable {
        Assert.notEmpty(urls, "找不到可用的服务提供者：" + type);
        // 开启netty连接，把连接对象放入protocolClientMap中
        for (URL url : urls) {
            openClient(url);
        }
        // 使用cglib创建代理对象，屏蔽远程调用的代价
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("toString".equals(name)) {
                    return null;
                } else if ("hashCode".equals(name)) {
                    return null;
                } else if ("equals".equals(name)) {
                    return null;
                }
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setInterfaceName(type.getName());
                rpcRequest.setMethodName(method.getName());
                rpcRequest.setArguments(args);
                rpcRequest.setParameterTypes(method.getParameterTypes());
                rpcRequest.setVersion(urls.get(0).getVersion());
                // 负载均衡
                Collection<ProtocolClient> clients = protocolClientMap.get(ProtocolUtils.serviceNodeKey(urls.get(0))).values();
                List<ProtocolClient> protocolClients=new ArrayList<>(clients.size());
                Assert.notEmpty(clients, "找不到可用的服务提供者：" + type);
                log.info("可用的客户端连接有{}个，", clients.size());
                for (ProtocolClient protocolClient : clients) {
                    protocolClients.add(protocolClient);
                }
                // 负载均衡选择客户端进行发送
                ProtocolClient client = loadBalance.select(
                        ProtocolUtils.exporterKey(urls.get(0).getInterfaceName(), urls.get(0).getVersion()), protocolClients);
                // 将rpcFuture对象存起来,key为请求的唯一标识
                RpcFuture rpcFuture = client.send(rpcRequest);
                pendingRpc.put(rpcRequest.getRequestId(), rpcFuture);
                return rpcFuture.get();
            }
        });
    }

    protected ProtocolServer createServer(URL url) throws Throwable {
        return new DubboProtocolServer(Transporters.bind(url, getChannelHandler()));
    }

    protected ProtocolClient createClient(URL url) {
        return new DubboProtocolClient(Transporters.connect(url, getChannelHandler()));
    }
}
