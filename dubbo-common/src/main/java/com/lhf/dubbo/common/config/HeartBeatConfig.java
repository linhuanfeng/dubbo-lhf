package com.lhf.dubbo.common.config;

import com.lhf.dubbo.common.bean.RpcRequest;

public class HeartBeatConfig {

    public int BEAT_INTERVAL;
    public int BEAT_TIMEOUT;
    public static final String BEAT_ID = "BEAT_PING_PONG";

    public static RpcRequest getPingRequest(){
        return BeatPing.beatPing;
    }

    static class BeatPing{
        static RpcRequest beatPing=new RpcRequest(BEAT_ID);
    }

    public int getBEAT_INTERVAL() {
        return BEAT_INTERVAL;
    }

    public void setBEAT_INTERVAL(int BEAT_INTERVAL) {
        this.BEAT_INTERVAL = BEAT_INTERVAL;
    }

    public int getBEAT_TIMEOUT() {
        return BEAT_TIMEOUT;
    }

    public void setBEAT_TIMEOUT(int BEAT_TIMEOUT) {
        this.BEAT_TIMEOUT = BEAT_TIMEOUT;
    }
}
