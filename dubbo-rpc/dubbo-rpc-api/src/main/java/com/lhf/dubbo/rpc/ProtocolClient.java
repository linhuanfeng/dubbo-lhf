package com.lhf.dubbo.rpc;


import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.remoting.Client;

/**
 * 对NettyClient的装饰，发送数据
 */
public interface ProtocolClient {
    // 客户端的唯一标识，等于channelId等于nettyChannelId
    String getId();

    RpcFuture send(RpcRequest message);
    Client getClient();
//    ExchangeClient getExchangeClient();
//    default ExchangeClient getClient() {
//        return null;
//    }

//    default void setRemotingServers(RemotingServer server) {
//    }

    String getAddress();

//    void setAddress(String address);

//    default URL getUrl() {
//        return null;
//    }

//    default void reset(URL url) {
//    }

//    void close();

//    Map<String, Object> getAttributes();
}
