package com.lhf.dubbo.remoting.transport.netty.codec;

import com.lhf.dubbo.remoting.transport.netty.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class RpcEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    public RpcEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            byte[] data = serializer.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }catch (Exception e){
//            log.error("序列化失败，{}"+e.getMessage());
            throw e;
        }
    }
}
