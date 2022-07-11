package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
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
    private URL url;
    public ChannelServerInitializer(NettyServerHandler nettyServerHandler,URL url) {
        this.nettyServerHandler=nettyServerHandler;
        this.url=url;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        NettyCodecAdapter adapter = new NettyCodecAdapter();
        ch.pipeline()
                .addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                .addLast("decoder", adapter.getDecoder(false))
                .addLast("encoder", adapter.getEncoder(false))
                .addLast("idle", new IdleStateHandler(0, 0, url.getHeartBeatConfig().getBEAT_TIMEOUT(), TimeUnit.SECONDS))
                .addLast("idleHandler", new ServerHeartBeatHandler())
                .addLast("nettyHandler", nettyServerHandler);
    }
}
