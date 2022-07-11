package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.transport.AbstractClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class NettyClient extends AbstractClient {
    private Bootstrap bootstrap;
    private Channel channel;

    public NettyClient(URL url, ChannelHandler handler) {
        super(url, handler);
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    protected void doOpen() {
        final NettyClientHandler nettyClientHandler = createNettyClientHandler(); // 对ChannelHandler进一步装饰
        bootstrap = new Bootstrap();
        initBootstrap(nettyClientHandler);
    }

    private void initBootstrap(NettyClientHandler nettyClientHandler) {
        try {
            Channel channel = bootstrap.channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup(2))
                    .handler(new ChannelClientInitializer(nettyClientHandler,getUrl()))
                    .connect(new InetSocketAddress(getUrl().getHost(), getUrl().getPort()))
                    .sync()
                    .channel();
            this.channel = channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private NettyClientHandler createNettyClientHandler() {
        return new NettyClientHandler(getUrl(), getHandler());
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public RpcFuture send(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if (!channelFuture.isSuccess()) {
                log.error("发送消息失败：{}",request);
            }
            return rpcFuture;
        } catch (InterruptedException e) {
            log.error("发送消息异常：{}",request);
            throw new RuntimeException(e);
        }
    }
}
