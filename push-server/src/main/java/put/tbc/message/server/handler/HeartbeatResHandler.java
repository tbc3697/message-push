package put.tbc.message.server.handler;

import put.tbc.message.api.MsgType;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.message.MessageHeader;
import put.tbc.message.server.ChannelCache;
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
            log.debug("receive heartbeat: {}", ctx.channel().remoteAddress());
            ctx.writeAndFlush(PushMessage.buildHeartbeatResponseEntity());
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("超过时间未读到消息，中断连接");
        ChannelCache.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable caught) {
        log.error("异常: {}, {}", caught.getClass(), caught.getMessage());
        ctx.fireExceptionCaught(caught);
    }

}
