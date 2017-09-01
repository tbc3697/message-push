package com.edusky.message.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/29 19:56:28.
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageHeader {

    public MessageHeader(byte type) {
        this.type = type;
    }

    /*
     * 消息校验码（crcCode= 固定值+主版本号+次版本号）：
     * 1. 固定值，表明该消息是自定义协议；
     * 2. 主版本号，1-255，1个字节；
     * 3. 次版本号，1-255，1个字节；
     */
//    private int crcCode = 0xabef0101;

    /**
     * 消息长度，包括消息头和消息体；
     */
    private int length;

    private long sessionId;

    /**
     * 消息类型：
     * 0：业务请求消息(指令信息);
     * 1：业务响应消息(server)；
     * 2：业务ONE-WAY消息（即是请求又是响应）
     * 3：握手请求（认证）消息；
     * 4：握手应答（认证）消息；
     * 5：心跳请求消息；
     * 6：心跳应答消息；
     */
    private byte type;

}
