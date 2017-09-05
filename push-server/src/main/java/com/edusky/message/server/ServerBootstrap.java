package com.edusky.message.server;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author tbc on 2017/9/5 10:05:39.
 */
@Slf4j
public class ServerBootstrap {
    public static void main(String[] args) {
        new PushServer().bind(new InetSocketAddress("192.168.1.178", 7007));
//        Sleeps.days(99);
    }
}
