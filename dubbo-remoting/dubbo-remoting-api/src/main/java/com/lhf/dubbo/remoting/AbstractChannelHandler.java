package com.lhf.dubbo.remoting;

public abstract class AbstractChannelHandler implements ChannelHandler{
    @Override
    public Channel getChannel() {
        return null;
    }

    @Override
    public void setChannel(io.netty.channel.Channel channel) {

    }

    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void reconnect(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    @Override
    public void sent(Object message) {

    }

    @Override
    public void sent(Channel channel, Object message) {

    }

    @Override
    public void received(Channel channel, Object message) {

    }

    @Override
    public void caught(Channel channel, Throwable exception) {

    }
}
