package put.tbc.message.server.handler;

import put.tbc.message.api.MsgType;
import put.tbc.message.api.message.MessageHeader;
import put.tbc.message.api.message.MsgIdentity;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.message.PushMessageContent;
import put.tbc.message.api.toolkit.Objs;
import put.tbc.message.server.ChannelCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息推送处理器
 *
 * @author tbc on 2017/9/4 10:40:43.
 */
@Slf4j
public class PushCommandHandler extends SimpleChannelInboundHandler<PushMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PushMessage msg) throws Exception {
        MessageHeader header = msg.getHeader();
        // 如果消息类型是业务推送请求，则处理
        if (Objs.nonEmpty(header) && MsgType.REQ.equals(header.getType())) {
            sendMsg(ctx, msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void sendMsg(ChannelHandlerContext ctx, PushMessage msg) {
        log.debug("********************************************************************");
        PushMessageContent content = msg.getBody();
        if (Objs.isEmpty(content)) {
            ctx.channel().writeAndFlush("消息体为空");
        }
        // 发送给谁
        MsgIdentity identity = content.getTo();
        if (Objs.isEmpty(identity)) {

        }
        Channel channel = getChannel(identity);
        if (Objs.isEmpty(channel)) {
            log.error("该设备未连接: {}", identity);
            return;
        }

        PushMessage request = PushMessage.buildRequestEntity();
        request.getBody().setFrom(content.getFrom());
        request.getBody().setTo(identity);
        request.getBody().setContentBody(content.getContentBody());
        channel.writeAndFlush(request);

        ctx.writeAndFlush(PushMessage.buildResponseEntity());
    }

    private Channel getChannel(MsgIdentity identity) {
        return ChannelCache.get(identity);
    }
}
