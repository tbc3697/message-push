package com.edusky.message.server;

import com.edusky.message.api.codec.MessageDecoder;
import com.edusky.message.api.codec.MessageEncoder;
import com.edusky.message.server.handler.HeartbeatResHandler;
import com.edusky.message.server.handler.LoginAuthResHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import pub.tbc.toolkit.Sleeps;

import java.io.IOException;

/**
 * @author tbc on 2017/8/29 11:43:49.
 */
@Slf4j
public class MessageServer {
    public static void main(String[] args) {
        new MessageServer().bind();
        Sleeps.days(99);
    }

    public void bind() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws IOException {
                            ch.pipeline().addLast(new MessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(15));
                            ch.pipeline().addLast("LoginAuthResHandler", new LoginAuthResHandler());
                            ch.pipeline().addLast("HeartBeatHandler", new HeartbeatResHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            b.bind(Constant.HOST, Constant.PORT).sync();
            log.info("MessagePush server start ok : {} : {}", Constant.HOST, Constant.PORT);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
//        finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
    }

}
