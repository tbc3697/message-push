package com.edusky.message.client.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.MessageHeader;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.toolkit.Objs;
import com.edusky.message.client.PushCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/9/4 20:09:40.
 */
@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<PushMessage> {
    private PushCallback callback;

    public MessageHandler(PushCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        MessageHeader header = msg.getHeader();
        if (Objs.nonEmpty(header) && MsgType.REQ.equals(header.getType())) {
            log.info("收到任务请求============================================>：{}", msg);
            callback.callback(msg);
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
