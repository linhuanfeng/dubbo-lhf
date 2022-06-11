package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.Client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class AbstractClient /**extends AbstractPeer**/ implements Client {
    protected volatile ExecutorService executor;
    private URL url;
    private ChannelHandler channelHandler;
    public AbstractClient(URL url, ChannelHandler channelHandler) {
        this.url=url;
        this.channelHandler=channelHandler;
        initExecutor(url);
        doOpen();
    }

    protected void initExecutor(URL url){
        executor= Executors.newCachedThreadPool();
    }

    protected abstract void doOpen();

    protected ChannelHandler getHandler() {
        return channelHandler;
    }

    protected URL getUrl() {
        return url;
    }
}
