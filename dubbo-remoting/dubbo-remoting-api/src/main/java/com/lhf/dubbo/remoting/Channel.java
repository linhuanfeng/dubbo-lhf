package com.lhf.dubbo.remoting;

import java.net.InetSocketAddress;

public interface Channel /**extends Endpoint**/{
    /**
     * get remote address.
     *
     * @return remote address.
     */
//    InetSocketAddress getRemoteAddress();

    /**
     * is connected.
     *
     * @return connected
     */
//    boolean isConnected();
    io.netty.channel.Channel getChannel();
    void setChannel(io.netty.channel.Channel channel);
    void sent(Object message);
}
