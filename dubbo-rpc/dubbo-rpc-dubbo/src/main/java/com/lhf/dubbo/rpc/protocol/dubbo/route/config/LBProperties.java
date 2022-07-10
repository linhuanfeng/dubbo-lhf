package com.lhf.dubbo.rpc.protocol.dubbo.route.config;

import com.lhf.dubbo.rpc.protocol.dubbo.route.LBEnum;

public class LBProperties {
    private static LBEnum lbType=LBEnum.LRU;

    public static LBEnum getLbType() {
        return lbType;
    }

    public void setLbType(LBEnum lbType) {
        this.lbType = lbType;
    }
}
