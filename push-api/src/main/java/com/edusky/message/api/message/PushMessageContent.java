package com.edusky.message.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/9/2 14:46:21.
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageContent {
    //length=36
    private String openId;
    // 认证时用，从token缓存中拿
    private String token;
    // 设备类型
    private byte deviceType;

    private Object contentBody;
}
