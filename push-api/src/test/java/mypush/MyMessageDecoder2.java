package mypush;

import com.edusky.message.api.message.MyPushMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author tbc on 2017/8/31 15:11:56.
 */
@Slf4j
public class MyMessageDecoder2 extends MessageToMessageDecoder<MyPushMessage> {


    private String getJSONString(ByteBuf in) {
        in.readInt();
        if (in.readableBytes() > 4) {
            return in.readCharSequence(in.readableBytes(), StandardCharsets.UTF_8).toString();
        }
        return null;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, MyPushMessage msg, List<Object> out) throws Exception {

    }
}
