package com.lhf.dubbo.remoting.exchange;

import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ExchangeClient;
import com.lhf.dubbo.remoting.ExchangeServer;

/**
 * 封装PRC调用模式，同步转异步
 */
public interface Exchanger {
    ExchangeServer bind(URL url, ExchangeHandler handler) throws Throwable;
    ExchangeClient connect(URL url, ExchangeHandler handler);
}
