package com.lhf.dubbo.remoting.transport.netty.serializer;

public enum SerializerEnum {
    proto("proto"),hessian("hessian");
    private String name;

    SerializerEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
