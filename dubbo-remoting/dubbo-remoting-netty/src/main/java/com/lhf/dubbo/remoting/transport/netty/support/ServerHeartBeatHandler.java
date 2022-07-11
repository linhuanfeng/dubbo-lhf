package com.lhf.dubbo.remoting.transport.netty.support;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    // 处理IdleStateHandler触发的空闲事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 超时，关闭channel,释放资源
        if (evt instanceof IdleStateEvent) {
            log.info("{},客户端的心跳超时，channel：{}关闭，{}",
                    evt, ctx.channel().id(), ctx.channel().remoteAddress());
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
