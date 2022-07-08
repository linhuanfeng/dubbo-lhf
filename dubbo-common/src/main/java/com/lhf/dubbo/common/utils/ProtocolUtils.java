package com.lhf.dubbo.common.utils;

import com.lhf.dubbo.common.bean.URL;
import org.springframework.util.StringUtils;

import static com.lhf.dubbo.common.bean.CommonConstants.*;

public class ProtocolUtils {
    /**
     * /root/interfaceName/version/provider/serviceName-00001
     * @param url
     * @return
     */
    public static String serviceKey(URL url){
        if(StringUtils.isEmpty(url.getServiceName())){
            url.setServiceName(url.getInterfaceName());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_SEPARATOR)
                .append(url.getRoot()).append(PATH_SEPARATOR)
                .append(url.getInterfaceName()).append(PATH_SEPARATOR)
                .append(url.getVersion()).append(PATH_SEPARATOR)
                .append(url.getType()).append(PATH_SEPARATOR)
                .append(url.getServiceName()).append(SEQUENTIAL_SEPARATOR);
        return sb.toString();
    }

    /**
     * /root/interfaceName/version/provider
     * @param url
     * @return
     */
    public static String serviceKeyParent(URL url){
        if(StringUtils.isEmpty(url.getServiceName())){
            url.setServiceName(url.getInterfaceName());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_SEPARATOR)
                .append(url.getRoot()).append(PATH_SEPARATOR)
                .append(url.getInterfaceName()).append(PATH_SEPARATOR)
                .append(url.getVersion()).append(PATH_SEPARATOR)
                .append(url.getType());
        return sb.toString();
    }

    public static String exporterKey(String interfaceName,String version){
        return interfaceName+EXPORTER_SEPARATOR+version;
    }
}
