package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.Client;
import com.lhf.dubbo.remoting.ExchangeClient;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

public class HeaderExchangeClient implements ExchangeClient {
    private Client client;

    public HeaderExchangeClient(Client client) {
        this.client = client;
    }

    @Override
    public RpcFuture send(RpcRequest message) {
        return client.send(message);
    }
}
