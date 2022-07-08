package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.common.bean.*;
import com.lhf.dubbo.common.utils.ProtocolUtils;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.exchange.ExchangeHandler;
import com.lhf.dubbo.remoting.exchange.ExchangeHandlerAdapter;
import com.lhf.dubbo.remoting.transport.netty.support.Exchangers;
import com.lhf.dubbo.rpc.*;
import com.lhf.dubbo.rpc.protocol.AbstractProtocol;
import com.lhf.dubbo.rpc.proxy.jdk.JdkProxyFactory;
import org.apache.log4j.Logger;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DubboProtocol extends AbstractProtocol {
    private Logger logger = Logger.getLogger(DubboProtocol.class);
    private Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();
    private Map<String, RpcFuture> pendingRpc = new ConcurrentHashMap<>();
    /**
     * 通过requestHandler进行层与层之间的传递
     */
    private ExchangeHandler exchangeHandler = new ExchangeHandlerAdapter() {
        @Override
        public void connected(Channel channel) {

        }

        @Override
        public void disconnected(Channel channel) {

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
                logger.debug("收到响应："+ rpcResponse);
                RpcFuture rpcFuture = pendingRpc.get(rpcResponse.getRequestId());
                if(rpcFuture!=null){
                    pendingRpc.remove(rpcResponse.getRequestId());
                    rpcFuture.setRpcResponse(rpcResponse);
                }else {
                    logger.debug("响应匹配失败："+ rpcResponse);
                }
            }else if (message instanceof RpcRequest) {
                RpcRequest request = (RpcRequest) message;
                logger.debug("收到请求："+ request);
                Invoker<?> invoker = getInvoker(request);
                Object res = invoker.invoke(request);
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                response.setResult(res);
                return response;
            }else {
                logger.debug("收到普通消息："+ message);
            }
            return null;
        }
    };

    Invoker<?> getInvoker(RpcRequest request) {
        String serviceName = request.getInterfaceName();
        String version = request.getVersion();
        Assert.hasText(serviceName, "serviceName不能为空,at "+request);
        Assert.hasText(version, "version不能为空,at "+request);
        Exporter<?> exporter = exporterMap.get(ProtocolUtils.exporterKey(serviceName,version));
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
        // 服务key
        String key = serviceKey(url);
        // 开启netty服务
        openServer(url);
        // 创建暴露对象
        Exporter<T> exporter = new DubboExporter<>(invoker);
//        exporterMap.put(NameGenerator.makeServiceName(invoker.getUrl().getInterfaceName(), invoker.getUrl().getVersion()), exporter);
        // 缓存暴露对象，key(interfaceName:version) value(expoter)
        exporterMap.put(ProtocolUtils.exporterKey(invoker.getUrl().getInterfaceName(),invoker.getUrl().getVersion()),exporter);

        return exporter;
    }

    /**
     * 生成客户端代理对象
     */
    @Override
    public Object refer(Class type, URL url) throws Throwable {
        openClient(url); // 开启netty服务

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
                rpcRequest.setVersion(url.getVersion());
                RpcFuture rpcFuture = protocolClient.send(rpcRequest);
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

    protected ProtocolClient createClient(URL url) throws Throwable {
        if (exchangeClient == null) {
            exchangeClient = Exchangers.connect(url, exchangeHandler);
        }
        if (protocolClient == null) {
            protocolClient = new DubboProtocolClient(exchangeClient);
        }
        return protocolClient;
    }
}
