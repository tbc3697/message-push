package pub.tbc.message.client;

import pub.tbc.message.client.handler.HeartbeatReqHandler;
import pub.tbc.message.client.handler.LoginAuthReqHandler;
import put.tbc.message.api.codec.PushMessageDecoder;
import put.tbc.message.api.codec.PushMessageEncoder;
import put.tbc.message.api.message.MsgIdentity;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.toolkit.Objs;
import put.tbc.message.api.toolkit.Sleeps;
import pub.tbc.message.client.handler.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author tbc on 2017/8/30 11:30:38.
 */
@Slf4j
@Data
public class PushClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();
    private PushCallback callback;
    private MsgIdentity identity;
    private Channel channel;

    PushClient(PushCallback callback, MsgIdentity identity) {
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
            this.channel = future.channel();
            log.info("connected server {}:{}", host, port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 所有资源释放完成后，清空资源，再次发起重边操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Sleeps.seconds(Constant.RECONNECT_TIME);
                    connect(Constant.REMOTE_IP, Constant.REMOTE_PORT);
                }
            });
        }
    }

    public void sendMsg(PushMessage message) {
        if (Objs.isEmpty(channel)) {
            throw new RuntimeException("TCP连接尚未建立");
        }
        channel.writeAndFlush(message);
    }
}
