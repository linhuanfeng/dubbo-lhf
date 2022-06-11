package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import org.apache.log4j.Logger;


/**
 * 抽象等级，子类有AbstractServer和AbstractChannel
 */
@Deprecated
public abstract class AbstractPeer implements /**Endpoint,**/ChannelHandler {
    private final ChannelHandler channelHandler;
    private Logger logger=Logger.getLogger(AbstractPeer.class);
    private volatile URL url;


    public AbstractPeer(URL url, ChannelHandler handler) {
        this.url = url;
        this.channelHandler = handler;
    }

    @Override
    public void sent(Object message) {
        logger.info("要发送的消息是:"+message);
    }


    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    @Override
    public void sent(Channel channel, Object message) {
        channelHandler.sent(channel, message);
    }

    @Override
    public void received(Channel channel, Object message) {
        channelHandler.received(channel, message);
    }

    @Override
    public void caught(Channel channel, Throwable exception) {

    }
}
