package pub.tbc.message.client;

import put.tbc.message.api.message.MsgIdentity;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.message.PushMessageContent;
import put.tbc.message.api.toolkit.Sleeps;
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

//        for (int i = 0; i < 3; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    {
//                        PushClient client = new PushBuilder()
//                                .callback(new PushCallback() {
//                                    @Override
//                                    public void callback(Object msg) {
//                                        System.out.println(msg);
//                                    }
//                                })
//                                .deviceType((byte) 2)
//                                .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
//                                .getClient();
//                        client.connect("192.168.1.178", 7007);
//
//                        new PushBuilder()
//                                .callback(new PushCallback() {
//                                    @Override
//                                    public void callback(Object msg) {
//                                        System.out.println(msg);
//                                    }
//                                })
//                                .deviceType((byte) 2)
//                                .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
//                                .getClient()
//                                .connect("192.168.1.178", 7007);
//                    }
//                }
//            }, "thread-" + i).start();
//        }


//        Sleeps.seconds(8);
        //初始化
        final PushClient client = new PushBuilder()
                .callback(new PushCallback() {
                    @Override
                    public void callback(Object msg) {
                        System.out.println(msg);
                    }
                })
                .deviceType((byte) 2)
                .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
                .getClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect("192.168.1.178", 7007);
            }
        }).start();
        Sleeps.seconds(2);
        // 构建消息
        PushMessage message = PushMessage.buildRequestEntity();
        PushMessageContent content = message.getBody();
        content.setFrom(client.getIdentity());
        content.setTo(
                MsgIdentity.builder()
                        .deviceType((byte) 2)
                        .openId("12345678990")
                        .build()
        );
        // 发消息
        client.sendMsg(message);

        Sleeps.hours(1);
    }
}
