package pub.tbc.message.client.handler;

import pub.tbc.message.client.PushCallback;
import put.tbc.message.api.MsgType;
import put.tbc.message.api.message.MessageHeader;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.toolkit.Objs;
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
