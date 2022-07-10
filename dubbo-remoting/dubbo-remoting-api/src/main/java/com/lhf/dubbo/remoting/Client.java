package com.lhf.dubbo.remoting;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.RpcResponse;
import io.netty.channel.Channel;

public interface Client /**extends /**Endpoint,**/ /**Channel**/{
    /**
     * reconnect.
     */
    void reconnect() ;
    RpcFuture send(RpcRequest message);
    Channel getChannel(); // 得到nettyChannel
}
