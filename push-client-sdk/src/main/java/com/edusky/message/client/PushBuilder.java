package com.edusky.message.client;

import com.edusky.message.api.message.MsgIdentity;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/9/2 15:12:08.
 */
@Slf4j
public class PushBuilder {
    private MsgIdentity identity = new MsgIdentity();
    private PushCallback callback;

    public PushBuilder openId(String openId) {
        identity.setOpenId(openId);
        return this;
    }

    public PushBuilder deviceType(byte type) {
        identity.setDeviceType(type);
        return this;
    }

    public PushBuilder token(String token) {
        identity.setToken(token);
        return this;
    }

    public PushBuilder callback(PushCallback callback) {

        return this;
    }

    public PushClient getClient() {
        return new PushClient(callback);
    }
}
