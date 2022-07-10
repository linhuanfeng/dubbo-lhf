package com.lhf.dubbo.remoting;

import com.lhf.dubbo.common.bean.URL;

import java.net.InetSocketAddress;

public interface Channel /**extends Endpoint**/{
    String getId(); // 唯一标识，方便后续识别channel并删除旧channel
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
    URL getUrl();
}
