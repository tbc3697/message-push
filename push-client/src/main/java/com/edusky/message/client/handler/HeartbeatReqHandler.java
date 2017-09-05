package com.edusky.message.client.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author tbc on 2017/8/30 14:13:11.
 */
@Slf4j
public class HeartbeatReqHandler extends SimpleChannelInboundHandler<PushMessage> {
    private volatile ScheduledFuture heartbeat;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        log.debug("收到服务器消息：{}", msg);
        MessageHeader header = msg.getHeader();
        // 如果握手响应消息，握手成功，主动发送心跳信息
        if (header != null && MsgType.LOGIN_RES.equals(header.getType())) {
            heartbeat = ctx.executor().scheduleAtFixedRate(
//                    new HeartbeatReqHandler.HeartbeatTask(ctx),
                    () -> {
                        log.debug("client send heart beat message to server: {}", heartbeat);
                        ctx.writeAndFlush(PushMessage.buildHeartbeatRequestEntity());
                    },
                    0,
                    5000,
                    TimeUnit.MILLISECONDS
            );
        } else if (header != null && MsgType.HEARTBEAT_RES.equals(header.getType())) {
            // 心跳响应消息
            log.debug("receive heartbeat message from server: {}", msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("超过时间未读到任何消息，中断连接, event: {}", evt);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable caught) {
        if (heartbeat != null) {
            heartbeat.cancel(true);
            heartbeat = null;
        }
        log.error("异常： {} - {}", caught.getClass(), caught.getMessage());
        ctx.fireExceptionCaught(caught);
    }

    private class HeartbeatTask implements Runnable {
        private ChannelHandlerContext context;

        HeartbeatTask(ChannelHandlerContext ctx) {
            this.context = ctx;
        }

        @Override
        public void run() {
            log.debug("client send heart beat message to server: {}", heartbeat);
            context.writeAndFlush(PushMessage.buildHeartbeatRequestEntity());
        }
    }


}
