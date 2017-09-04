package com.edusky.message.client;

import com.edusky.message.api.codec.PushMessageDecoder;
import com.edusky.message.api.codec.PushMessageEncoder;
import com.edusky.message.api.message.MsgIdentity;
import com.edusky.message.api.toolkit.Sleeps;
import com.edusky.message.client.handler.HeartbeatReqHandler;
import com.edusky.message.client.handler.LoginAuthReqHandler;
import com.edusky.message.client.handler.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author tbc on 2017/8/30 11:30:38.
 */
@Slf4j
public class PushClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();
    private PushCallback callback;
    private MsgIdentity identity;

    public PushClient(PushCallback callback, MsgIdentity identity) {
        this.callback = callback;
        this.identity = identity;
    }

    public void connect(String host, int port) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("pushMessageDecoder", new PushMessageDecoder());
                            ch.pipeline().addLast("pushMessageEncoder", new PushMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("loginAuthReqHandler", new LoginAuthReqHandler(identity));
                            ch.pipeline().addLast("heartbeatReqHandler", new HeartbeatReqHandler());
                            ch.pipeline().addLast("messageProcessHandler", new MessageHandler(callback));
                        }
                    });
            // 异步连接
            ChannelFuture future = b.connect(new InetSocketAddress(host, port)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 所有资源释放完成后，清空资源，再次发起重边操作
            executor.execute(() -> {
                Sleeps.seconds(Constant.RECONNECT_TIME);
                connect(Constant.REMOTE_IP, Constant.REMOTE_PORT);
            });
        }
    }

    public static void main(String[] args) {
        new PushClient(System.out::println, null).connect(Constant.REMOTE_IP, Constant.REMOTE_PORT);
    }

}
