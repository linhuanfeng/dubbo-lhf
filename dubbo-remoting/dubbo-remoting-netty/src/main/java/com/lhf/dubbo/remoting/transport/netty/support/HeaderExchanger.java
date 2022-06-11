package com.lhf.dubbo.remoting.transport.netty.support;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.remoting.exchange.ExchangeHandler;
import com.lhf.dubbo.remoting.exchange.Exchanger;

/**
 * 交换层对象，可获取交换层服务器ExchangeServer
 */
public class HeaderExchanger implements Exchanger {

    @Override
    public ExchangeClient connect(URL url, ExchangeHandler handler) {
        return new HeaderExchangeClient(Transporters.connect(url,handler));
    }

    @Override
    public ExchangeServer bind(URL url, ExchangeHandler handler) throws Throwable {
        return new HeaderExchangeServer(Transporters.bind(url,handler));
    }
}
