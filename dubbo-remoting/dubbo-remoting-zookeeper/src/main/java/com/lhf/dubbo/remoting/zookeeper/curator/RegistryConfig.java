package com.lhf.dubbo.remoting.zookeeper.curator;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@ConfigurationProperties(prefix = "dubbo.registry")
public class RegistryConfig {
    private String connectString = "127.0.0.1:2181";
    private int sessionTimeout = 100000;
    private final Charset CHARSET= StandardCharsets.UTF_8;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Charset getCHARSET() {
        return CHARSET;
    }
}
