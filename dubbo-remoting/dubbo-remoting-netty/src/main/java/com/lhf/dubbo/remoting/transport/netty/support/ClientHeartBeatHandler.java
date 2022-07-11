package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.config.HeartBeatConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHeartBeatHandler extends ChannelInboundHandlerAdapter {
    // 处理IdleStateHandler触发的空闲事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 发送心跳包
        RpcRequest beatPing = HeartBeatConfig.getPingRequest();
//        log.info("event:{},channelId:{},发送心跳包：{}",evt,ctx.channel().id(),beatPing.getRequestId());
        ctx.writeAndFlush(beatPing); // 不加。channel因为不需要之后的出栈处理器处理
        super.userEventTriggered(ctx, evt);
    }
}
