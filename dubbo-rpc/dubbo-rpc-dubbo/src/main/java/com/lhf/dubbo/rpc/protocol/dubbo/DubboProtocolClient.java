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
package com.lhf.dubbo.rpc.protocol.dubbo;

import com.lhf.dubbo.common.bean.RpcFuture;
import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.remoting.Client;
import com.lhf.dubbo.rpc.ProtocolClient;


public class DubboProtocolClient implements ProtocolClient {

//    private ExchangeClient exchangeClient; // headerExchangeClient
    private Client client;
    private String address;

    @Override
    public String getId() {
        return null;
    }

    public RpcFuture send(RpcRequest message){
        return client.send(message);
    }

//    public DubboProtocolClient(ExchangeClient exchangeClient) {
//        this.exchangeClient = exchangeClient;
//    }

    public DubboProtocolClient(Client client) {
        this.client = client;
    }

//    @Override
//    public ExchangeClient getExchangeClient() {
//        return exchangeClient;
//    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
