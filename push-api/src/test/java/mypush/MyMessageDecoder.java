package mypush;

import com.alibaba.fastjson.JSON;
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
        log.info("decode byteBuf length : {}", in.readableBytes());
        print(in);

        ByteBuf frame = (ByteBuf) super.decode(context, in);
        if (frame == null) return null;
        String json = getJSONString(frame);
        return JSON.parseObject(json, MyPushMessage.class);
    }

    private String getJSONString(ByteBuf in) {
        int a = in.readInt();
        if (in.readableBytes() > 4) {
            String str = in.readCharSequence(in.readableBytes(), StandardCharsets.UTF_8).toString();
            log.info("msg : {} ", str);
            return str;
        }
        return null;
    }

    private void print(ByteBuf in) {
        System.out.println("=====================" + in.getInt(0));
        System.out.println("--------------------0" + in.getByte(0));
        System.out.println("--------------------1" + in.getByte(1));
        System.out.println("--------------------2" + in.getByte(2));
        System.out.println("--------------------3" + in.getByte(3));
    }
}
