package com.lhf.dubbo.remoting;

/**
 * 对netty的channelhandler进一步封装
 */
public interface ChannelHandler {
    void setChannel(Channel channel);
    Channel getChannel();
//    io.netty.channel.Channel getChannel();
    /**
     * on channel connected.
     *
     * @param channel channel.
     */
//    void connected(Channel channel) ;

//    void reconnect(Channel channel);
    /**
     * on message received.
     *
     * @param channel channel.
     * @param message message.
     */
    void received(Channel channel, Object message);
    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    void disconnected(Channel channel) ;

    /**
     * on exception caught.
     *
     * @param channel   channel.
     * @param exception exception.
     */
//    void caught(Channel channel, Throwable exception) ;

}
