package com.lhf.dubbo.config.spring.config;

import com.lhf.dubbo.common.bean.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消费者的公共配置
 */
@ConfigurationProperties(prefix = "dubbo.consumer")
public class ReferenceConfig {
    private Integer retries=3;
    private Integer beatInternal= CommonConstants.BEAT_INTERNAL;
//    private Integer beatTimeout=3;

    public Integer getBeatInternal() {
        return beatInternal;
    }

    public void setBeatInternal(Integer beatInternal) {
        this.beatInternal = beatInternal;
    }

//    public Integer getBeatTimeout() {
//        return beatTimeout;
//    }
//
//    public void setBeatTimeout(Integer beatTimeout) {
//        this.beatTimeout = beatTimeout;
//    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }
}
