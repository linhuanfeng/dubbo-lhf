package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.common.bean.*;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.Client;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.exchange.ExchangeHandler;
import com.lhf.dubbo.remoting.exchange.ExchangeHandlerAdapter;
import com.lhf.dubbo.remoting.transport.netty.NettyClient;
import com.lhf.dubbo.remoting.transport.netty.support.Exchangers;
import com.lhf.dubbo.rpc.*;
import com.lhf.dubbo.rpc.protocol.AbstractProtocol;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LBExtension;
import com.lhf.dubbo.rpc.protocol.dubbo.route.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DubboProtocol extends AbstractProtocol {
    private Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();
    private Map<String, RpcFuture> pendingRpc = new ConcurrentHashMap<>();
    // 统一使用spi方式获取负载均衡器
    private LoadBalance loadBalance = LBExtension.getLoadBalance();
    /**
     * 通过requestHandler进行层与层之间的传递,
     * 被多个netty连接共享，是否线程安全
     */
    private ExchangeHandler exchangeHandler = new ExchangeHandlerAdapter() {
        @Override
        public void connected(Channel channel) {

        }

        @Override
        public void disconnected(Channel channel) {
            // channel断开，exporterMap移除可用连接，同时，考虑支持重连
            log.info("channel:{}",channel.getChannel());

        }

        @Override
        public void sent(Channel channel, Object message) {
        }

        @Override
        public void received(Channel channel, Object message) {
            Object result = reply(channel, message); // 处理消息
            if (result != null) {
                channel.sent(result); // 响应消息
            }
        }

        @Override
        public void caught(Channel channel, Throwable exception) {

        }

        @Override
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
                if(Beat.BEAT_ID.equals(request.getRequestId())){
                    log.info("channel:{},收到心跳，无需恢复",channel);
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

    private void channelReconnect(Channel channel){
        log.info("channel断开，开启自动重连");
        io.netty.channel.Channel nettyChannel = channel.getChannel();
        URL url = channel.getUrl();
        try {
            // 移除旧数据
            String clientKey=ProtocolUtils.serviceNodeKey(url);
            List<ProtocolClient> clientList = protocolClientMap.get(clientKey);
            for (ProtocolClient protocolClient : clientList) {
                ExchangeClient exchangeClient = protocolClient.getExchangeClient();
                Client client = exchangeClient.getClient(); // nettyClient
                // 替换新的client
//                client.
            }
            // 重连
            openClient(url);
        } catch (Throwable e) {
            log.error("netty连接异常");
            throw new RuntimeException(e);
        }
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
        // 开启netty服务
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
        Assert.notEmpty(urls, "没有找到对应的服务提供者：" + type);
        // 开启netty连接
        for (URL url : urls) {
            openClient(url);
        }
        // 服务代理，调用远程服务
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
                List<ProtocolClient> protocolClients = protocolClientMap.get(ProtocolUtils.serviceNodeKey(urls.get(0)));
                log.info("可用的客户端连接有{}个，", protocolClients.size());
                RpcFuture rpcFuture = loadBalance.select(ProtocolUtils.exporterKey(urls.get(0).getInterfaceName(),urls.get(0).getVersion()),protocolClients).send(rpcRequest);
                pendingRpc.put(rpcRequest.getRequestId(), rpcFuture);
                return rpcFuture.get();
            }
        });
    }

    /**
     * 得到交换层的ExchangeServer对象
     *
     * @param url
     * @return
     */
    protected ProtocolServer createServer(URL url) throws Throwable {
        if (exchangeServer == null) {
            exchangeServer = Exchangers.bind(url, exchangeHandler);
        }
        if (protocolServer == null) {
            protocolServer = new DubboProtocolServer(exchangeServer);
        }
        return protocolServer;
    }

    protected ProtocolClient createClient(URL url) {
//        if (exchangeClient == null) {
//            exchangeClient = Exchangers.connect(url, exchangeHandler);
//        }
        ProtocolClient protocolClient = new DubboProtocolClient(Exchangers.connect(url, exchangeHandler));
        return protocolClient;
    }
}
