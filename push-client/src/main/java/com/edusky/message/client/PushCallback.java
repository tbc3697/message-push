package com.edusky.message.client;


/**
 * @author tbc on 2017/9/2 15:08:40.
 */
@FunctionalInterface
public interface PushCallback {
    void callback(Object msg);
}
