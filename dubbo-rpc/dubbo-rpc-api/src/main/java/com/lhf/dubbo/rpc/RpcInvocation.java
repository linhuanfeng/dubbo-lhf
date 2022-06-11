/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lhf.dubbo.rpc;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * RPC Invocation.
 *
 * @serial Don't change the class name and properties.
 */
@Deprecated
public class RpcInvocation implements Invocation, Serializable {

    private static final long serialVersionUID = -4355285085441097045L;

    private String protocolServiceKey;

    private String methodName;

    private String interfaceName;

    private transient Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Invoker<?> invoker;

    private transient Class<?> returnType;

    private transient Type[] returnTypes;

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getServiceName() {
        return interfaceName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    public String getProtocolServiceKey() {
        return protocolServiceKey;
    }

    public void setProtocolServiceKey(String protocolServiceKey) {
        this.protocolServiceKey = protocolServiceKey;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Invoker<?> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
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

    @Override
    public String toString() {
        return "RpcInvocation{" +
                "protocolServiceKey='" + protocolServiceKey + '\'' +
                ", methodName='" + methodName + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", arguments=" + Arrays.toString(arguments) +
                ", invoker=" + invoker +
                ", returnType=" + returnType +
                ", returnTypes=" + Arrays.toString(returnTypes) +
                '}';
    }
}
