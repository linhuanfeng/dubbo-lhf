package com.lhf.dubbo.rpc;

import com.lhf.dubbo.remoting.RemoteServer;
public interface ProtocolServer {
    RemoteServer getRemoteServer();
//    default ExchangeServer getExchangeServer() {
//        return null;
//    }

//    default void setRemotingServers(RemotingServer server) {
//    }

//    String getAddress();

//    void setAddress(String address);

//    default URL getUrl() {
//        return null;
//    }

//    default void reset(URL url) {
//    }

//    void close();

//    Map<String, Object> getAttributes();
}
