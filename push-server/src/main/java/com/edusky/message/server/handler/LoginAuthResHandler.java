package com.edusky.message.server.handler;

import com.edusky.message.api.MsgType;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/30 11:35:09.
 */
@Slf4j
public class LoginAuthResHandler extends SimpleChannelInboundHandler<PushMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        /*对msg进行验证，可以获取token比对*/
        MessageHeader header = msg.getHeader();
        if (header == null) {

        }
        assert header != null;
        // 握手请求信息，处理，其它，透传
        if (MsgType.LOGIN_REQ.equals(header.getType())) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            //1. 验证token；
            //2. 查找channel列表，判断是否重复；
            //3. 若未重复，建立连接，并保存到channel列表；
            //4. 若重复，断开旧连接，重复3；

            ctx.writeAndFlush(buildResponse());
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    private PushMessage buildResponse() {
        return new PushMessage(new MessageHeader(MsgType.LOGIN_RES.getCode()));
    }

}
