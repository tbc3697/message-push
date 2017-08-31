package com.edusky.message.api.codec;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/29 20:19:21.
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<PushMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PushMessage msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new RuntimeException("The encode message is null");
        }
        buildBuf(out, msg);
    }

    private void buildBuf(ByteBuf sendBuf, PushMessage msg) {
        MessageHeader header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionId());
        sendBuf.writeByte(header.getType());

        if (msg.getBody() != null) {
            sendBuf.writeBytes(getBytes(msg.getBody()));
        } else {
            sendBuf.writeInt(0);
        }
        // 回写长度，索引4的位置，是消息的长度
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }

    private void jsonEncode(Object value, ByteBuf buf) {
        buf.writeBytes(JSON.toJSONString(value).getBytes());
    }

    private byte[] getBytes(Object value) {
        return JSON.toJSONString(value).getBytes();
    }


}
