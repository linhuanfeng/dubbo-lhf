package com.lhf.dubbo.remoting.transport.netty;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.rpc.RpcInvocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler {
    private final URL url;

    private final ChannelHandler handler;
    public NettyClientHandler(URL url, ChannelHandler handler){
        this.url=url;
        this.handler=handler;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        handler.received(NettyChannel.getOrAddChannel(ctx.channel(),url,handler),msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel注册成功：{}",ctx);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel注销：{}",ctx);
        super.channelUnregistered(ctx);
        handler.
        // 通知dubboProtocol,移除可用的channel
//        handler.disconnected(NettyChannel.getOrAddChannel(ctx.channel(),url,handler));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
