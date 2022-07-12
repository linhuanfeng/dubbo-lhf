package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import sun.net.util.URLUtil;

public abstract class AbstractNettyChannel implements Channel {
    private io.netty.channel.Channel channel;
    private URL url;
    private ChannelHandler channelHandler;

    public AbstractNettyChannel(io.netty.channel.Channel channel, URL url, ChannelHandler channelHandler) {
        this.channel = channel;
        this.url = url;
        this.channelHandler = channelHandler;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public void setChannelHandler(ChannelHandler channelHandler) {
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
