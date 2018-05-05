package put.tbc.message.api;

import lombok.Getter;

/**
 * 消息类型
 *
 * @author tbc on 2017/8/29 15:46:16.
 */
public enum MsgType {
    REQ((byte) 0, "request"),
    RES((byte) 1, "response"),
    ONE_WAY((byte) 2, "onw-way"),
    LOGIN_REQ((byte) 3, "login request"),
    LOGIN_RES((byte) 4, "login response"),
    HEARTBEAT_REQ((byte) 5, "heartbeat request"),
    HEARTBEAT_RES((byte) 6, "heartbeat response");

    MsgType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    @Getter
    private byte code;
    @Getter
    private String name;


    public boolean equals(byte o) {
        return o == code;
    }

}
