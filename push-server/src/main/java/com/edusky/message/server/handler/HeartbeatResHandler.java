package com.edusky.message.server.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/30 14:13:11.
 */
@Slf4j
public class HeartbeatResHandler extends SimpleChannelInboundHandler<PushMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        MessageHeader header = msg.getHeader();
        if (header != null && MsgType.HEARTBEAT_REQ.equals(header.getType())) {
            log.debug("receive heartbeat ");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable caught) {
        ctx.fireExceptionCaught(caught);
    }

}
