package com.lhf.dubbo.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.UUID;
@Data
public class RpcRequest implements Serializable {
    private String requestId;
    private String protocolServiceKey;

    private String methodName;

    private String interfaceName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Class<?> returnType;

    private transient Type[] returnTypes;

    private String version;
    public RpcRequest() {
        this(UUID.randomUUID().toString());
    }
    public RpcRequest(String requestId) {
        this.requestId = requestId;
    }
}
