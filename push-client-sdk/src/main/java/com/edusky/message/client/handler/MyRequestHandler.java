package com.edusky.message.client.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.MessageHeader;
import com.edusky.message.api.message.MyPushMessage;
import com.edusky.message.api.message.PushMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author tbc on 2017/8/31 15:37:04.
 */
@Slf4j
public class MyRequestHandler extends SimpleChannelInboundHandler {
    private volatile ScheduledFuture heartbeat;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("TCP握手成功，发送验证消息...");
        ctx.writeAndFlush(MyPushMessage.builder().flag((byte) 0).build());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("receive message : {}", msg);
        MyPushMessage message = (MyPushMessage) msg;
        if (message != null) {
            switch (message.getFlag()) {
                case 1:
                    break;
                case 2:
                    log.info("receive heartbeat msg from server ");
                    break;
                case 4:
                    log.info("收到业务消息，需要处理");
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("异常： {} - {}", cause.getClass(), cause.getMessage());
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

    private void heartbeat(MyPushMessage message, ChannelHandlerContext ctx) {
        heartbeat = ctx.executor().scheduleAtFixedRate(
                new MyRequestHandler.HeartbeatTask(ctx),
                0,
                5000,
                TimeUnit.MILLISECONDS
        );
    }


    private class HeartbeatTask implements Runnable {
        private ChannelHandlerContext context;

        HeartbeatTask(ChannelHandlerContext ctx) {
            this.context = ctx;
        }

        @Override
        public void run() {
            PushMessage heartbeatPushMessage = new PushMessage();
            log.debug("client send heart beat message to server: {}", heartbeat);
            context.writeAndFlush(buildHeartBeat());
        }

        private PushMessage buildHeartBeat() {
            PushMessage pushMessage = new PushMessage();
            MessageHeader header = new MessageHeader();
            header.setType(MsgType.HEARTBEAT_REQ.getCode());
            pushMessage.setHeader(header);
            return pushMessage;
        }
    }


}
