package com.edusky.message.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Object body;

    public PushMessage(MessageHeader header) {
        this.header = header;
    }
}
