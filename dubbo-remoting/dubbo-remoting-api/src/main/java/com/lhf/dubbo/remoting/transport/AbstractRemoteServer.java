package com.lhf.dubbo.remoting.transport;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.RemoteServer;

import java.net.InetSocketAddress;

public abstract class AbstractRemoteServer /**extends AbstractEndpoint**/ implements RemoteServer {
    private InetSocketAddress bindAddress;
//    private ExecutorService executor;
    private URL url;
    private ChannelHandler channelHandler;
    public AbstractRemoteServer(URL url, ChannelHandler channelHandler) throws Throwable {
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
