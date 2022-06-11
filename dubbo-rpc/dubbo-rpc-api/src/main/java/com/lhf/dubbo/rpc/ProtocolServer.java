/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lhf.dubbo.rpc;


import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.ExchangeServer;
import com.lhf.dubbo.remoting.RemotingServer;

import java.util.Map;

/**
 * Distinct from {@link RemotingServer}, each protocol holds one or more ProtocolServers(the number usually decides by port numbers),
 * while each ProtocolServer holds zero or one RemotingServer.
 */
public interface ProtocolServer {

    default ExchangeServer getExchangeServer() {
        return null;
    }

//    default void setRemotingServers(RemotingServer server) {
//    }

//    String getAddress();

//    void setAddress(String address);

//    default URL getUrl() {
//        return null;
//    }

//    default void reset(URL url) {
//    }

//    void close();

//    Map<String, Object> getAttributes();
}
