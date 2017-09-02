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

    public static DeviceType getDeviceType(int type) {
        switch (type) {
            case 1:
                return WEB;
            case 2:
                return PHONE;
            case 3:
                return TV;
            case 4:
                return PAD;
            default:
                throw new RuntimeException("不是有效的设备类型");
        }
    }
}
