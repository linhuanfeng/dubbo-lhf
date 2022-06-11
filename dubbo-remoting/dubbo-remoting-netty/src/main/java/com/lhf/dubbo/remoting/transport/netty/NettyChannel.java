package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.transport.AbstractChannel;
import io.netty.channel.Channel;

public class NettyChannel extends AbstractChannel {

    public NettyChannel(Channel channel,URL url, ChannelHandler handler) {
        super(channel,url, handler);
    }

    public static NettyChannel getOrAddChannel(io.netty.channel.Channel channel, URL url, ChannelHandler handler) {
        return new NettyChannel(channel,url,handler);
    }
}
