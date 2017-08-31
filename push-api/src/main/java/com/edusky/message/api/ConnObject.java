package com.edusky.message.api;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/8/29 15:44:56.
 */
@Slf4j
@Data
public class ConnObject {

    private String openId;
    private DeviceType deviceType;
}
