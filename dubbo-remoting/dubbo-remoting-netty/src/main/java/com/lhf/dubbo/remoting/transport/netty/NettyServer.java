package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.transport.AbstractRemoteServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

/**
 * 本身也是ChannelHandler()
 */
public class NettyServer extends AbstractRemoteServer {

    private ServerBootstrap bootstrap;
    //    private Map<String, Channel> channels; // <ip:port, channel>
    private io.netty.channel.Channel channel;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    public NettyServer(URL url, ChannelHandler handler) throws Throwable {
        super(url, handler);
    }

    @Override
    public boolean isBound() {
        return false;
    }

    @Override
    public Collection<Channel> getChannels() {
        return null;
    }

    @Override
    public Channel getChannel(InetSocketAddress remoteAddress) {
        return null;
    }

    @Override
    protected void doOpen() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(2);
        workerGroup = new NioEventLoopGroup(2);
        bootstrap = new ServerBootstrap();
        // NettyHandler持有ChannelHandler,ChannelHandler是各层之间消息传送的中介
        final NettyServerHandler nettyServerHandler = new NettyServerHandler(getUrl(), getChannelHandler()); // 因为本身ChannelHandler对象
        bootstrap.channel(NioServerSocketChannel.class)
                .group(bossGroup, workerGroup)
                .childHandler(new ChannelServerInitializer(nettyServerHandler,getUrl()));
        this.channel = bootstrap.bind(getBindAddress()).sync().channel();
    }

    @Override
    protected void doClose() throws Throwable {

    }

    public io.netty.channel.Channel getChannel() {
        return channel;
    }
}
