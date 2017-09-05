package com.edusky.message.client;

import com.edusky.message.api.message.MsgIdentity;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.PushMessageContent;
import com.edusky.message.api.toolkit.Loops;
import com.edusky.message.api.toolkit.Sleeps;
import com.sun.javafx.geom.transform.Identity;
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
        this.callback = callback;
        return this;
    }

    public PushClient getClient() {
        return new PushClient(callback, identity);
    }

    public static void main(String[] args) {

        Loops.times(999, i -> {
            new Thread(() -> {
                PushClient client = new PushBuilder()
                        .callback(System.out::println)
                        .deviceType((byte) 2)
                        .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
                        .getClient();
                client.connect("192.168.1.178", 7007);
            }, "thread-" + i).start();
        });
        Sleeps.seconds(8);
        PushClient client = new PushBuilder()
                .callback(System.out::println)
                .deviceType((byte) 2)
                .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
                .getClient();
        new Thread(() -> client.connect("192.168.1.178", 7007)).start();
        Sleeps.seconds(2);
        PushMessage message = PushMessage.buildRequestEntity();
        PushMessageContent content = message.getBody();
        content.setFrom(client.getIdentity());
        content.setTo(MsgIdentity.builder().deviceType((byte) 2).openId("edu-space-blueSky-message-thread-2").build());
        client.sendMsg(message);
    }
}
