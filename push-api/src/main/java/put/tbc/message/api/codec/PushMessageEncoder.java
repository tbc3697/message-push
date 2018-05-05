package put.tbc.message.api.codec;

import com.alibaba.fastjson.JSON;
import put.tbc.message.api.message.MessageHeader;
import put.tbc.message.api.message.PushMessage;
import put.tbc.message.api.toolkit.Objs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 编码规则
 * <pre>
 * +----+----------+-----------+-----------+------------+
 * |字段 | Length   | sessionId |   type    | body       |
 * +----+----------+-----------+-----------+------------+
 * |长度 | int:4byte| long:8byte|  byte:1   | jsonEntity |
 * +----+----------+-----------+-----------+------------+
 * |方法 |writeInt()|writeLong()|writeByte()|writeBytes()|
 * +----+----------+-----------+-----------+------------+
 * </pre>
 */
@Slf4j
public class PushMessageEncoder extends MessageToByteEncoder<PushMessage> {
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
        // 长度，暂且0，处理完消息体重写
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
//            out.setInt(0, value.length + 9);
        }
        out.setInt(0, out.readableBytes() - 4);

    }

    private byte[] getJSONBytes(Object o) {
        return JSON.toJSONString(o).getBytes(StandardCharsets.UTF_8);
    }
}
