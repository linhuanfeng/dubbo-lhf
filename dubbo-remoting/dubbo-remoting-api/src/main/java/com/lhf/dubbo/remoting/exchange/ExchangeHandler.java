package com.lhf.dubbo.remoting.exchange;

import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.ExchangeChannel;

import java.util.concurrent.CompletableFuture;

public interface ExchangeHandler extends ChannelHandler {
    Object reply(Channel channel, Object message);
    Channel getChannel();
    void setChannel(Channel channel);
}
