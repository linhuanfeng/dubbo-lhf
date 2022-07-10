package com.lhf.dubbo.rpc.protocol.dubbo.route;

public enum LBEnum {
    robbin("robbin"),LRU("lru");
    private String name;

    LBEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
