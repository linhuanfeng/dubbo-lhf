package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.Client;
import com.lhf.dubbo.remoting.RemotingServer;
import com.lhf.dubbo.remoting.Transporter;
import com.lhf.dubbo.remoting.transport.netty.NettyClient;
import com.lhf.dubbo.remoting.transport.netty.NettyServer;

/**
 * 传输层对象，可获取NettyServer服务器
 */
public class NettyTransporter implements Transporter {

    @Override
    public RemotingServer bind(URL url, ChannelHandler handler) throws Throwable {
        return new NettyServer(url,handler);
    }

    @Override
    public Client connect(URL url, ChannelHandler handler){
        return new NettyClient(url, handler);
    }
}
