package com.lhf.dubbo.common.bean;

public enum ServiceType {
    provider("provider"),consumer("consumer"),registry("registry");
    private String type;

    ServiceType(String provider) {
        this.type=provider;
    }
}
