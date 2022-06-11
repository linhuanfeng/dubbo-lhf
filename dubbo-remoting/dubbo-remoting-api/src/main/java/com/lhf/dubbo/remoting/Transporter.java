package com.lhf.dubbo.remoting;

import com.lhf.dubbo.common.bean.URL;

/**
 * netty网络传输
 */
public interface Transporter {
    RemotingServer bind(URL url, ChannelHandler handler) throws Throwable;
    Client connect(URL url, ChannelHandler handler);
}
