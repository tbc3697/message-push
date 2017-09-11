package mypush;

import com.alibaba.fastjson.JSON;
import com.edusky.message.api.message.MyPushMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author tbc on 2017/8/31 15:08:47.
 */
@Slf4j
public class MyMessageEncoder extends MessageToByteEncoder<MyPushMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyPushMessage msg, ByteBuf out) throws Exception {
        log.debug("encode Object msg : {} ", msg);
        byte[] message = getJSONBytes(msg);
        out.writeInt(message.length); // 加上长度消息本身的长度
        out.writeBytes(message);
        log.info("编码消息长度： {}", out.readableBytes());
    }

    private byte[] getJSONBytes(Object o) {
        return JSON.toJSONString(o).getBytes(StandardCharsets.UTF_8);
    }
}
