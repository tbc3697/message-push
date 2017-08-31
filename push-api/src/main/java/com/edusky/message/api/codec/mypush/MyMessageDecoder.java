package com.edusky.message.api.codec.mypush;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author tbc on 2017/8/31 15:11:56.
 */
@Slf4j
public class MyMessageDecoder extends LengthFieldBasedFrameDecoder {
    public MyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }


    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(context, in);
        if (frame == null) return null;
        return getJSONString(in);
    }

    private String getJSONString(ByteBuf in) {
        in.readInt();
        if (in.readableBytes() > 4) {
            return in.readCharSequence(in.readableBytes(), StandardCharsets.UTF_8).toString();
        }
        return null;
    }
}
