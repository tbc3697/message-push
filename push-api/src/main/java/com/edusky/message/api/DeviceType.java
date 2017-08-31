package com.edusky.message.api;

import lombok.Getter;

/**
 * @author tbc on 2017/8/29 15:41:16.
 */
public enum DeviceType {
    WEB(1, "web"), PHONE(2, "手机"), TV(3, "TV"), PAD(4, "PAD");

    DeviceType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @Getter
    private int type;
    @Getter
    private String name;
}
