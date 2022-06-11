package com.lhf.dubbo.common.bean;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.UUID;

public class RpcRequest implements Serializable {
    private String requestId= UUID.randomUUID().toString();
    private String protocolServiceKey;

    private String methodName;

    private String interfaceName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Class<?> returnType;

    private transient Type[] returnTypes;

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", protocolServiceKey='" + protocolServiceKey + '\'' +
                ", methodName='" + methodName + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", arguments=" + Arrays.toString(arguments) +
                ", returnType=" + returnType +
                ", returnTypes=" + Arrays.toString(returnTypes) +
                '}';
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProtocolServiceKey() {
        return protocolServiceKey;
    }

    public void setProtocolServiceKey(String protocolServiceKey) {
        this.protocolServiceKey = protocolServiceKey;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Type[] getReturnTypes() {
        return returnTypes;
    }

    public void setReturnTypes(Type[] returnTypes) {
        this.returnTypes = returnTypes;
    }
}
