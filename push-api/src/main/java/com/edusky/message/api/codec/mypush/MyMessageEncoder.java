package com.edusky.message.api.codec.mypush;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.message.MyPushMessage;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/31 15:08:47.
 */
@Slf4j
public class MyMessageEncoder extends MessageToByteEncoder<MyPushMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyPushMessage msg, ByteBuf out) throws Exception {
        log.debug("encode Object msg : {} ", msg);
        byte[] message = getJSONBytes(msg);
        out.writeInt(message.length + 4); // 加上长度消息本身的长度
        out.writeBytes(message);
    }

    private byte[] getJSONBytes(Object o) {
        return JSON.toJSONString(o).getBytes(Charsets.UTF_8);
    }
}
