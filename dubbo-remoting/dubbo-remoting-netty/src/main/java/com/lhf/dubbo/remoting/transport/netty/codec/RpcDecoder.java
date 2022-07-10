package com.lhf.dubbo.remoting.transport.netty.codec;

import com.lhf.dubbo.remoting.transport.netty.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
//import lombok.extern.slf4j.Slf4j;

import java.util.List;

//@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    private Serializer serializer;
    private Class<?> genericClass; // 解码的时候需要class,对于客户端对于的是RpcResponse.class,服务端的是RpcRequest.class

    public RpcDecoder(Serializer serializer,Class<?> genericClass) {
        this.serializer = serializer;
        this.genericClass=genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if(in.readableBytes()<4){
                // 规定LengthFieldBasedFrameDecoder的帧长度占4个字节
                return;
            }
            in.markReaderIndex(); // 标记当前读的位置
            int dataLength = in.readInt();
            if(in.readableBytes()<dataLength){
                in.resetReaderIndex();
                return;
            }
            byte[] data = new byte[dataLength];
            in.readBytes(data);
            Object obj= serializer.deserialize(data, genericClass);// 解码入站的rpc请求,客户端和服务端对应的class是不一样的
            out.add(obj);
        }catch (Exception e){
//            log.error("反序列化失败，{}"+e.getMessage());
            throw e;
        }
    }
}
