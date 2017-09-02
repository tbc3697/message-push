package com.edusky.message.server.handler;

import com.edusky.message.api.message.MessageHeader;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.toolkit.Objs;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于发消息的处理器
 *
 * @author tbc on 2017/9/2 18:22:15.
 */
@Slf4j
public class PushMessageHandler extends SimpleChannelInboundHandler<PushMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        MessageHeader header = msg.getHeader();
        if (Objs.isEmpty(header)) {
            throw new RuntimeException("message head is null!");
        }
        byte type = header.getType();
    }
}
