package com.edusky.message.api.codec;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.message.MessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author tbc on 2017/8/29 20:52:02.
 */
@Slf4j
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(context, in);
        if (frame == null) return null;

        PushMessage pushMessage = new PushMessage();

        pushMessage.setHeader(
                MessageHeader.builder()
                        .length(in.readInt())
                        .sessionId(in.readLong())
                        .type(in.readByte())
                        .build()
        );

        if (in.readableBytes() > 4) {
            pushMessage.setBody(jsonDecode(frame));
        }
        return pushMessage;
    }

    private Object jsonDecode(ByteBuf buf) {
        String str = buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString();
        return JSON.parseObject(str);
    }

}
