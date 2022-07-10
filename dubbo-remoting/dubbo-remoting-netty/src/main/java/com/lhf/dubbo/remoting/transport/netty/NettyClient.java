package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.transport.AbstractClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;
import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

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

    public void write(Object message){
        channel.writeAndFlush(message);
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
                    .handler(new ChannelClientInitializer(nettyClientHandler))
                    .connect(new InetSocketAddress(getUrl().getHost(),getUrl().getPort()))
                    .sync()
                    .channel();
            this.channel=channel;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private NettyClientHandler createNettyClientHandler() {
        return new NettyClientHandler(getUrl(),getHandler());
    }
int count=1;
    @Override
    public void reconnect() {
        // 这里自动重连比较方便科学
        log.info("netty重连第{}次",count++);
        doOpen();
    }

    @Override
    public RpcFuture send(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if(!channelFuture.isSuccess()){
//                logger.error("发送消息失败：{}",request);
            }
            return rpcFuture;
        } catch (InterruptedException e) {
//            logger.error("发送消息异常：{}",request);
            throw new RuntimeException(e);
        }
    }
}
