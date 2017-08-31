package com.edusky.message.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/31 15:40:47.
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPushMessage {
    //类型  系统编号 0xAB 表示A系统，0xBC 表示B系统
    private byte type;

    /**
     * 0. 连接认证消息；
     * 1. 连接认证响应消息；
     * 2. 心跳请求消息；
     * 3. 心跳响应消息；
     * 4. 业务请求消息；
     * 5. 业务响应消息；
     * 6. 指令请求消息；
     * 7. 指令响应消息；
     */
    private byte flag;

    //主题信息的长度
    private int length;

    //主题信息
    private String body;
}
