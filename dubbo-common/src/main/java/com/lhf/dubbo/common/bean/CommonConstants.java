package com.lhf.dubbo.common.bean;

public interface CommonConstants {
    String SEQUENTIAL_SEPARATOR="-"; // 分割zookeeper连续结点的序号
    String PATH_SEPARATOR="/";
    String EXPORTER_SEPARATOR=":";
    int BEAT_INTERNAL=2;
    int BEAT_TIMEOUT=BEAT_INTERNAL*3;
}
