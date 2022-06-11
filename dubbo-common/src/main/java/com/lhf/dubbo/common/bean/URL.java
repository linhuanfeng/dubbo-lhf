package com.lhf.dubbo.common.bean;

import java.io.Serializable;

import static com.lhf.dubbo.common.bean.CommonConstants.*;

public class URL implements Serializable {
    String root="dubbo-lhf";
    String interfaceName;
    ServiceType type;
    String serviceName; // 默认等于interfaceName
    String version="1.0.0";
    String host;
    Integer port;

    public URL() {
    }

    public URL(String root, String interfaceName, ServiceType type, String serviceName, String version, String host, Integer port) {
        this.root = root;
        this.interfaceName = interfaceName;
        this.type = type;
        this.serviceName = serviceName;
        this.version = version;
        this.host = host;
        this.port = port;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
