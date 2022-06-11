package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.RemotingServer;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

public abstract class AbstractServer /**extends AbstractEndpoint**/ implements RemotingServer {
    private InetSocketAddress bindAddress;
//    private ExecutorService executor;
    private URL url;
    private ChannelHandler channelHandler;
    public AbstractServer(URL url, ChannelHandler channelHandler) throws Throwable {
//        super(url, handler);
        this.url=url;
        this.channelHandler=channelHandler;
        this.bindAddress=new InetSocketAddress(url.getHost(),url.getPort());
        doOpen();
    }
    protected URL getUrl() {
        return url;
    }
    protected ChannelHandler getChannelHandler(){
        return channelHandler;
    }
    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(InetSocketAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    /**
     * 根据抽象模版方法模式，其实调用的是子类的doOpen方法
     * @throws Throwable
     */
    protected abstract void doOpen() throws Throwable;

    protected abstract void doClose() throws Throwable;
}
