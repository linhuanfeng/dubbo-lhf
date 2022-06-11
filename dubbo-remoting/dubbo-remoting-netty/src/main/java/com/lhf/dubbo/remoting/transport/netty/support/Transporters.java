package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ChannelHandler;
import com.lhf.dubbo.remoting.Client;
import com.lhf.dubbo.remoting.RemotingServer;
import com.lhf.dubbo.remoting.Transporter;

/**
 * 传输层入口：获取NettyServer对象
 * 工具类：调用NettyTransporter. bind(URL,ChannelHandler)得到NettyServer对象
 */
public class Transporters {
    public static RemotingServer bind(String url, ChannelHandler handler) throws Throwable {
        URL url1 = new URL();
        return getTransporter(url1).bind(url1,handler);
    }

    /**
     * 默认返回NettyServer
     * 根据NettyTransporter得到NettyServer对象
     * @param url
     * @param handler
     * @return
     */
    public static RemotingServer bind(URL url,ChannelHandler handler) throws Throwable {
        return getTransporter(url).bind(url,handler);
    }

    public static Client connect(String url, ChannelHandler handler) {
        URL url1 = new URL();
        return connect(url1, handler);
    }

    public static Client connect(URL url, ChannelHandler handler){
        return getTransporter(url).connect(url, handler);
    }

    // 根据SPI机制得到NettyTransporter传输器对象
    public static Transporter getTransporter(URL url) {
        return new NettyTransporter();
    }
}
