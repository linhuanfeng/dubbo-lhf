package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.remoting.exchange.ExchangeHandler;
import com.lhf.dubbo.remoting.exchange.Exchanger;

/**
 * 交换层入口：获取ExchangeServer对象
 * 工具类：调用HeaderExchanger.bind(URL,ExchangeHandler)得到ExchangeServer
 */
public class Exchangers {
    //调用HeaderExchanger.bind返回ExchangeServer对象
    public static ExchangeServer bind(URL url, ExchangeHandler handler) throws Throwable {
        return getExchanger(url).bind(url,handler);
    }
    public static ExchangeClient connect(URL url, ExchangeHandler handler){
        return getExchanger(url).connect(url, handler);
    }

    /**
     * 返回HeaderExchanger对象
     * @param url
     * @return
     */
    public static Exchanger getExchanger(URL url) {
        return new HeaderExchanger();
    }
}
