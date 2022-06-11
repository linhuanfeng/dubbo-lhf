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
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyClient extends AbstractClient {
    private Bootstrap bootstrap;
    private Channel channel;
    private Logger logger= LoggerFactory.getLogger(NettyClient.class);
    public NettyClient(URL url, ChannelHandler handler) {
        super(url, handler);
    }

    public void write(Object message){
        channel.writeAndFlush(message);
    }

    @Override
    protected void doOpen() {
        final NettyClientHandler nettyClientHandler = createNettyClientHandler();
        bootstrap = new Bootstrap();
        initBootstrap(nettyClientHandler);
    }

    private void initBootstrap(NettyClientHandler nettyClientHandler) {
        try {
            Channel channel = bootstrap.channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup(2))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler());
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers
                                    .weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            ch.pipeline().addLast("ecoder", new ObjectEncoder());
                            ch.pipeline().addLast("handler", nettyClientHandler);
                        }
                    })
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

    @Override
    public void reconnect() {

    }

    @Override
    public RpcFuture send(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if(!channelFuture.isSuccess()){
                logger.error("发送消息失败：{}",request);
            }
            return rpcFuture;
        } catch (InterruptedException e) {
            logger.error("发送消息异常：{}",request);
            throw new RuntimeException(e);
        }
    }
}
