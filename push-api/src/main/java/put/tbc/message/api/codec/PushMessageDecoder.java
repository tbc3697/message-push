package put.tbc.message.api.codec;

import com.alibaba.fastjson.JSON;
import put.tbc.message.api.message.MessageHeader;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.message.PushMessageContent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author tbc on 2017/8/31 15:11:56.
 */
@Slf4j
public class PushMessageDecoder extends LengthFieldBasedFrameDecoder {
    public PushMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    /**
     * 长度位置、长度、单包最大容量写死
     */
    public PushMessageDecoder() {
        super(1024 * 1024, 0, 4);
    }


    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(context, in);
        if (frame == null) return null;

        /* 开始解码 */
        PushMessage pushMessage = new PushMessage();
        MessageHeader header = new MessageHeader();
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());

        pushMessage.setHeader(header);
        // 为什么是大于4？编码时如果消息体为空，我们writeInt(0)写了一个值为0的int4byte
        if (frame.readableBytes() > 4) {
            String body = getJSONString(frame);
            pushMessage.setBody(getBody(body));
        }
        return pushMessage;
    }

    private PushMessageContent getBody(String json) {
        return JSON.parseObject(json, PushMessageContent.class);
    }

    private String getJSONString(ByteBuf in) {
        return in.readCharSequence(in.readableBytes(), StandardCharsets.UTF_8).toString();
    }
}
