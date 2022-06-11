package com.lhf.dubbo.common.bean;

import java.io.Serializable;

public class RpcProtocol implements Serializable {
    private String host;
    private Integer port;
    private String interfaceName;
    private String ImplName;

    public RpcProtocol(String host, Integer port, String interfaceName, String implName) {
        this.host = host;
        this.port = port;
        this.interfaceName = interfaceName;
        ImplName = implName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getImplName() {
        return ImplName;
    }

    public void setImplName(String implName) {
        ImplName = implName;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", interfaceName='" + interfaceName + '\'' +
                ", ImplName='" + ImplName + '\'' +
                '}';
    }
}
