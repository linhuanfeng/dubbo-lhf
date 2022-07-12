package com.lhf.dubbo.remoting;

import com.lhf.dubbo.common.bean.URL;


/**
 * 对nettyChannel的封装
 */
public interface Channel {
    /**
     * nettyChannel的id，方便后续识别channel并删除旧channel
     * @return
     */
    String getId();

    /**
     * is connected.
     *
     * @return connected
     */
//    boolean isConnected();
    io.netty.channel.Channel getChannel();
    void setChannel(io.netty.channel.Channel channel);
    void sent(Object message);
    URL getUrl();
}
