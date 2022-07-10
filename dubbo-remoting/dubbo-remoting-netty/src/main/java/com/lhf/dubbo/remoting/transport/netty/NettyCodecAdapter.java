package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.RpcResponse;
import com.lhf.dubbo.remoting.transport.netty.codec.RpcDecoder;
import com.lhf.dubbo.remoting.transport.netty.codec.RpcEncoder;
import com.lhf.dubbo.remoting.transport.netty.config.SerializerProperties;
import com.lhf.dubbo.remoting.transport.netty.serializer.Serializer;
import com.lhf.dubbo.remoting.transport.netty.serializer.SerializerEnum;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于SPI机制，得到不同的序列化器
 */
public class NettyCodecAdapter {
    private volatile Map<String, Serializer> serializerMap;
    private Object object = new Object();

    private void init() {
        if (serializerMap == null) {
            synchronized (object) {
                if (serializerMap == null) {
                    serializerMap = new ConcurrentHashMap<>();
                    // spi机制获取序列化器
                    ServiceLoader<Serializer> serializers = ServiceLoader.load(Serializer.class);
                    for (Serializer serializer : serializers) {
                        serializerMap.put(serializer.getType().getName(), serializer);
                    }
                }
            }
        }
    }

    public ChannelHandler getEncoder(boolean isClient) {
        init();
        Serializer serializer = serializerMap.get(SerializerProperties.getSerializerType().getName());
        return new RpcEncoder(serializer);
    }

    public ChannelHandler getDecoder(boolean isClient) {
        init();
        Serializer serializer = serializerMap.get(SerializerProperties.getSerializerType().getName());
        return new RpcDecoder(serializer, isClient ? RpcResponse.class : RpcRequest.class);
    }
}
