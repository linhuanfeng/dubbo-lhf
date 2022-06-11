package com.lhf.dubbo.remoting;

public interface ChannelHandler {
    Channel getChannel();
    void setChannel(io.netty.channel.Channel channel);
    /**
     * on channel connected.
     *
     * @param channel channel.
     */
    void connected(Channel channel) ;

    /**
     * on channel disconnected.
     *
     * @param channel channel.
     */
    void disconnected(Channel channel) ;

    void sent(Object message);

    /**
     * on message sent.
     *
     * @param channel channel.
     * @param message message.
     */
    void sent(Channel channel, Object message) ;

    /**
     * on message received.
     *
     * @param channel channel.
     * @param message message.
     */
    void received(Channel channel, Object message);

    /**
     * on exception caught.
     *
     * @param channel   channel.
     * @param exception exception.
     */
    void caught(Channel channel, Throwable exception) ;

}
