package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.annotation.RpcService;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.ServiceType;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.config.ServiceConfig;
import com.lhf.dubbo.registry.zookeeper.RegistryProtocol;
import com.lhf.dubbo.remoting.zookeeper.curator.RegistryConfig;
import com.lhf.dubbo.rpc.protocol.dubbo.ProtocolConfig;
import com.lhf.dubbo.rpc.proxy.AbstractProxyInvoker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 扫描标注RpcService的服务，并服务暴露和服务注册
 */
public class ServiceBean extends ServiceConfig implements ApplicationContextAware, InitializingBean {
    private RegistryProtocol registryProtocol;
    private RegistryConfig registryConfig;
    private ProtocolConfig protocolConfig;

    public ServiceBean(RegistryConfig registryConfig,ProtocolConfig protocolConfig) {
        this.registryConfig = registryConfig;
        this.protocolConfig=protocolConfig;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init();
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(!CollectionUtils.isEmpty(serviceMap)){
            for (Object serviceBean : serviceMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                URL url = generateUrl(rpcService);
                Class<?> claz = rpcService.interfaceClass();
                AbstractProxyInvoker<Object> invoker = new AbstractProxyInvoker<Object>(serviceBean,claz ,url ) {
                    @Override
                    public Object invoke(RpcRequest request) {
                        Method method = null;
                        try {
                            method = serviceBean.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
                            return method.invoke(serviceBean,request.getArguments());
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                try {
                    registryProtocol.export(invoker);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取服务暴露的url
     * @param rpcService
     * @return
     */
    private URL generateUrl(RpcService rpcService) {
        URL url = new URL();
        url.setHost(protocolConfig.getHost());
        url.setPort(protocolConfig.getPort());
        url.setType(ServiceType.provider);
        url.setInterfaceName(rpcService.interfaceClass().getName());
        url.setVersion(rpcService.version());
        return url;
    }

    @Override
    public void export() {
    }

    public void afterPropertiesSet() throws Exception {
    }

    private void init() {
        if(registryProtocol==null){
            registryProtocol=new RegistryProtocol(registryConfig);
        }
    }
}
