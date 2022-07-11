package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.common.config.HeartBeatConfig;
import com.lhf.dubbo.remoting.transport.netty.support.ClientHeartBeatHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChannelClientInitializer extends ChannelInitializer<SocketChannel> {
    private NettyClientHandler nettyClientHandler;
    private URL url;
    public ChannelClientInitializer(NettyClientHandler nettyClientHandler,URL url) {
        this.nettyClientHandler = nettyClientHandler;
        this.url=url;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        NettyCodecAdapter adapter = new NettyCodecAdapter();
        ch.pipeline()
                .addLast(new LoggingHandler(LogLevel.DEBUG))
                .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                .addLast("decoder", adapter.getDecoder(true))
                .addLast("ecoder", adapter.getEncoder(true))
                .addLast("idle", new IdleStateHandler(0, 0, url.getHeartBeatConfig().getBEAT_INTERVAL(), TimeUnit.SECONDS))
                .addLast("idleHandler", new ClientHeartBeatHandler())
                .addLast("handler", nettyClientHandler);
    }
}
