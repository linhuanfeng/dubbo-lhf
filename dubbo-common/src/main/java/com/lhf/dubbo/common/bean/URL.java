package com.lhf.dubbo.common.bean;

import com.lhf.dubbo.common.config.HeartBeatConfig;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

import static com.lhf.dubbo.common.bean.CommonConstants.*;

@Data
@ToString
public class URL implements Serializable {
    private String id=UUID.randomUUID().toString();
    private String root="dubbo-lhf";
    private String interfaceName;
    private ServiceType type;
    private String serviceName; // 默认等于interfaceName
    private String version;
    private String host;
    private Integer port;
    private HeartBeatConfig heartBeatConfig;
    private Integer retries;
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
}
