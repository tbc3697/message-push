package com.edusky.message.client;

import com.edusky.message.api.codec.mypush.MyMessageDecoder;
import com.edusky.message.api.codec.mypush.MyMessageEncoder;
import com.edusky.message.api.toolkit.Sleeps;
import com.edusky.message.client.handler.TestRequestHandler;
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
public class TestMessageClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("MyMessageDecoder", new MyMessageDecoder(1024 * 1024, 0, 4));
                            ch.pipeline().addLast("MyMessageEncoder", new MyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("MyRequestHandler", new TestRequestHandler());
                        }
                    });
            // 异步连接
//            ChannelFuture future = b.connect(
//                    new InetSocketAddress(host, port),
//                    new InetSocketAddress(Constant.LOCAL_IP, Constant.LOCAL_PORT)
//            ).sync();
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
        new TestMessageClient().connect(Constant.REMOTE_IP, Constant.REMOTE_PORT);
//        Sleeps.days(1);
    }

}
