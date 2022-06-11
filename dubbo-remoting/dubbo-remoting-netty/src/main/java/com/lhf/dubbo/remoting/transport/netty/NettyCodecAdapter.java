package com.lhf.dubbo.remoting.transport.netty;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyCodecAdapter {
    public ChannelHandler getEncoder(){
        return new ObjectEncoder();
    }
    public ChannelHandler getDecoder(){
        return new ObjectDecoder(ClassResolvers.weakCachingResolver(this.getClass().getClassLoader()));
    }
}
