package com.lhf.dubbo.remoting.transport.netty.config;


import com.lhf.dubbo.remoting.transport.netty.serializer.SerializerEnum;

public class SerializerProperties {
    private static SerializerEnum serializerType=SerializerEnum.hessian;

    public static SerializerEnum getSerializerType() {
        return serializerType;
    }

    public static void setSerializerType(SerializerEnum serializerType) {
        serializerType = serializerType;
    }
}
