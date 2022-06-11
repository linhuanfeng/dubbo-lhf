package com.lhf.dubbo.remoting;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;

public interface ExchangeClient /**extends Client**/{
    RpcFuture send(RpcRequest message);
}
