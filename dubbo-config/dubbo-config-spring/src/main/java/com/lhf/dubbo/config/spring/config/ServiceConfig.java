package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.bean.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消费者的公共配置
 */
@ConfigurationProperties(prefix = "dubbo.provider")
public class ServiceConfig {
//    private Integer retries=3;
//    private Integer beatInternal=10; // 服务端只需要心跳超时时间
    private Integer beatTimeout= CommonConstants.BEAT_TIMEOUT;

    public Integer getBeatTimeout() {
        return beatTimeout;
    }

    public void setBeatTimeout(Integer beatTimeout) {
        this.beatTimeout = beatTimeout;
    }

//    public Integer getRetries() {
//        return retries;
//    }
//
//    public void setRetries(Integer retries) {
//        this.retries = retries;
//    }
}
