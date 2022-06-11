package com.lhf.dubbo.rpc;

@Deprecated
public interface Invocation {
    String getMethodName();
    /**
     * get the interface name
     *
     * @return
     */
    String getServiceName();
    Class<?>[] getParameterTypes();
    Object[] getArguments();
}
