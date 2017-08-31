package com.edusky.message.server.client;

import io.netty.channel.Channel;

/**
 * @author tbc on 2017/8/29 11:49:15.
 */
public interface ClientService {

    int onlineCount();

    int addChannel(Channel channel);



}
