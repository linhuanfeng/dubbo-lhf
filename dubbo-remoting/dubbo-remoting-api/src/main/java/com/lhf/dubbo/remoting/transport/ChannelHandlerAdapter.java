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
package com.lhf.dubbo.remoting.transport;


import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ChannelHandler;

/**
 * ChannelHandlerAdapter.
 */
public class ChannelHandlerAdapter implements ChannelHandler {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    @Override
    public void setChannel(io.netty.channel.Channel channel) {
        this.channel.setChannel(channel);
    }

    @Override
    public void connected(Channel channel) {
        setChannel(channel);
    }

    @Override
    public void disconnected(Channel channel) {
    }

    @Override
    public void sent(Object message) {
    }

    @Override
    public void sent(Channel channel, Object message) {
    }

    @Override
    public void received(Channel channel, Object message)  {
    }

    @Override
    public void caught(Channel channel, Throwable exception)  {
    }

}
