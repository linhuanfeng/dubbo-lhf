package com.lhf.dubbo.remoting.transport.netty.support;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    // 处理IdleStateHandler触发的空闲事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 超时，关闭channel,释放资源
        log.info("event:{},说到客户端的心跳超时，服务器断开channel连接:{}",evt,ctx.channel().id());
        ctx.close();
    }
}
