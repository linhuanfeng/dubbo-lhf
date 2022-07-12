package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.transport.AbstractNettyChannel;
import io.netty.channel.Channel;

public class NettyChannel extends AbstractNettyChannel {

    public NettyChannel(Channel channel,URL url, ChannelHandler handler) {
        super(channel,url, handler);
    }

    public static NettyChannel getChannel(io.netty.channel.Channel channel, URL url, ChannelHandler handler) {
        return new NettyChannel(channel,url,handler);
    }

    @Override
    public String getId() {
        return getChannel().id().asShortText();
    }
}
