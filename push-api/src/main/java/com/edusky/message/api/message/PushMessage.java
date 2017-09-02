package com.edusky.message.api.message;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/29 19:56:18.
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushMessage {
    private MessageHeader header;
    private PushMessageContent body;

    public PushMessage(MessageHeader header) {
        this.header = header;
    }

    public static PushMessage buildAuthRequestEntity() {
        return new PushMessage(new MessageHeader((byte) 3));
    }

    public static PushMessage buildAuthResponseEntity() {
        return new PushMessage(new MessageHeader((byte) 4));
    }

    public static PushMessage buildHeartbeatRequestEntity() {
        return new PushMessage(new MessageHeader((byte) 5));
    }

    public static PushMessage buildHeartbeatResponseEntity() {
        return new PushMessage(new MessageHeader((byte) 5));
    }
}
