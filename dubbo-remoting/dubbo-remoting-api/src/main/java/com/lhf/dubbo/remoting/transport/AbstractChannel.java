package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import sun.net.util.URLUtil;

public abstract class AbstractChannel /**extends AbstractPeer**/ implements Channel {
    private io.netty.channel.Channel channel;
    private URL url;
    private ChannelHandler channelHandler;

    public AbstractChannel(io.netty.channel.Channel channel, URL url, ChannelHandler channelHandler) {
        this.channel = channel;
        this.url = url;
        this.channelHandler = channelHandler;
    }

    @Override
    public io.netty.channel.Channel getChannel() {
        return channel;
    }

    @Override
    public void setChannel(io.netty.channel.Channel channel) {
        this.channel=channel;
    }

    @Override
    public void sent(Object message) {
        channel.writeAndFlush(message);
    }
}