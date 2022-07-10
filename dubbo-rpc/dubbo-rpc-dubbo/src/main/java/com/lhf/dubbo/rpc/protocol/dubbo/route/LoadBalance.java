package com.lhf.dubbo.rpc.protocol.dubbo.route;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.rpc.ProtocolClient;

import java.util.List;

public interface LoadBalance {
    LBEnum getType();
    ProtocolClient select(String serviceKey, List<ProtocolClient> urlList);
}
