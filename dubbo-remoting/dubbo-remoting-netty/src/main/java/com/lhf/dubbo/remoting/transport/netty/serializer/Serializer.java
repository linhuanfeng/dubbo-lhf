package com.lhf.dubbo.remoting.transport.netty.serializer;


public interface Serializer {
    SerializerEnum getType();
    <T> byte[] serialize(T obj);
    <T> T deserialize(byte[] data, Class<T> clazz);
}
