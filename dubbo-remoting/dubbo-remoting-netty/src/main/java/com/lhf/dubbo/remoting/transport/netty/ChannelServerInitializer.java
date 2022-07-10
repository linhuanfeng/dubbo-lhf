package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.Beat;
import com.lhf.dubbo.remoting.transport.netty.support.ClientHeartBeatHandler;
import com.lhf.dubbo.remoting.transport.netty.support.ServerHeartBeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChannelServerInitializer extends ChannelInitializer<SocketChannel> {
    private NettyServerHandler nettyServerHandler;
    public ChannelServerInitializer(NettyServerHandler nettyServerHandler) {
        this.nettyServerHandler=nettyServerHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        NettyCodecAdapter adapter = new NettyCodecAdapter();
        ch.pipeline()
                .addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                .addLast("decoder", adapter.getDecoder(false))
                .addLast("encoder", adapter.getEncoder(false))
                .addLast("idle", new IdleStateHandler(0, 0, 4, TimeUnit.MILLISECONDS))
                .addLast("idleHandler", new ServerHeartBeatHandler())
                .addLast("nettyHandler", nettyServerHandler);
    }
}
