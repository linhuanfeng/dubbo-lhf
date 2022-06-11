package com.lhf.dubbo.remoting.exchange;

import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ExchangeChannel;
import com.lhf.dubbo.remoting.transport.ChannelHandlerAdapter;

import java.util.concurrent.CompletableFuture;

public abstract class ExchangeHandlerAdapter extends ChannelHandlerAdapter implements ExchangeHandler{
    @Override
    public abstract Object reply(Channel channel, Object message);

    @Override
    public void setChannel(Channel channel) {
        super.setChannel(channel);
    }

    @Override
    public Channel getChannel() {
        return super.getChannel();
    }

}
