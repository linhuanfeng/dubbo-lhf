package com.lhf.dubbo.remoting;


public abstract class AbstractChannelHandler implements ChannelHandler{
    protected Channel channel;

    @Override
    public void setChannel(Channel channel) {
        this.channel=channel;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
