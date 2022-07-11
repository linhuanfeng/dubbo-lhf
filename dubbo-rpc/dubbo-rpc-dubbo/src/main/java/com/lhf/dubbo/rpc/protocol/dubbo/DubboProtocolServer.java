package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.remoting.RemoteServer;
import com.lhf.dubbo.rpc.ProtocolServer;


public class DubboProtocolServer implements ProtocolServer {

    private RemoteServer remoteServer;
    public DubboProtocolServer(RemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }
    public RemoteServer getRemoteServer() {
        return remoteServer;
    }

}
