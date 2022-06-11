package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.remoting.RemotingServer;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * 交换层服务端，持有传输层NettyServer对象
 */
public class HeaderExchangeServer implements ExchangeServer {
    private final RemotingServer server;
    public HeaderExchangeServer(RemotingServer server) {
        this.server=server;
    }
}
