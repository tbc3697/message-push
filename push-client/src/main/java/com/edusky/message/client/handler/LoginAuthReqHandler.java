package com.edusky.message.client.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.MsgIdentity;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import com.edusky.message.api.message.PushMessageContent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/30 09:43:08.
 */
@Slf4j
public class LoginAuthReqHandler extends SimpleChannelInboundHandler<PushMessage> {
    private MsgIdentity identity;

    public LoginAuthReqHandler(MsgIdentity identity) {
        this.identity = identity;
    }

    /**
     * TCP三次握手成功后，发送
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(PushMessage.buildAuthRequestEntity(PushMessageContent.builder().from(identity).build()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        log.debug("收到服务器消息：{}", msg);
        if (msg == null) {
            throw new RuntimeException("receive and decoded message object is null");
        }
        MessageHeader header = msg.getHeader();
        // 如果是握手应答消息，要判断是否认证成功
        if (header != null && MsgType.LOGIN_RES.equals(header.getType())) {
            log.debug("receive heartbeat message: {}", msg);
            ctx.fireChannelRead(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("异常： {} - {}", cause.getClass(), cause.getMessage());
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }


}
