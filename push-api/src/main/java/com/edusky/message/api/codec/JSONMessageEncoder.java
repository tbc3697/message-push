package com.edusky.message.api.codec;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.message.MessageHeader;
import com.edusky.message.api.message.PushMessage;
import com.edusky.message.api.toolkit.Objs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author tbc on 2017/8/31 15:08:47.
 */
@Slf4j
public class JSONMessageEncoder extends MessageToByteEncoder<PushMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, PushMessage msg, ByteBuf out) throws Exception {
        log.debug("encode Object msg : {} ", msg);
        if (msg == null) {
            throw new RuntimeException("message is null!");
        }
        MessageHeader header = msg.getHeader();
        if (header == null) {
            throw new RuntimeException("message header is null!");
        }
        // 写消息头
        // 长度，暂且0，回头重写
        out.writeInt(0);
        //
        out.writeLong(header.getSessionId());
        // 写消息类型，1byte
        out.writeByte(header.getType());

        Object body = msg.getBody();
        if (Objs.isEmpty(body)) {
            out.writeInt(0);
        } else {
            byte[] value = getJSONBytes(msg.getBody());
            out.writeBytes(value);
            //+9是加上sessionId和消息类型的长度，半包解码计算长度从长度字段的后一个字节开始
            out.setInt(0, value.length + 9);
        }
    }

    private byte[] getJSONBytes(Object o) {
        return JSON.toJSONString(o).getBytes(StandardCharsets.UTF_8);
    }
}
