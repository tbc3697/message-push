package com.edusky.message.client.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/30 09:43:08.
 */
@Slf4j
public class LoginAuthReqHandler extends SimpleChannelInboundHandler {
    /**
     * TCP三次握手成功后，发送
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        PushMessage pushMessage = (PushMessage) msg;
        if (pushMessage == null) {

        }
        // 如果是握手应答消息，要判断是否认证成功
        assert pushMessage != null;
        if (pushMessage.getHeader() != null && MsgType.LOGIN_RES.equals(pushMessage.getHeader().getType())) {
            byte loginResult = (byte) pushMessage.getBody();
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                ctx.close();
            } else {
                log.debug("Login is Ok: ", pushMessage);
                ctx.fireChannelRead(msg);
            }
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

    private PushMessage buildLoginReq() {
        PushMessage pushMessage = new PushMessage();
        MessageHeader header = new MessageHeader();
        header.setType(MsgType.LOGIN_REQ.getCode());
        pushMessage.setHeader(header);
        return pushMessage;
    }


}
