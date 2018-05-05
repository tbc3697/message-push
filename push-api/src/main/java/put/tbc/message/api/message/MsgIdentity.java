package put.tbc.message.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/9/4 10:38:08.
 */
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MsgIdentity {
    // length=36
    private String openId;
    // 认证时用，从token缓存中拿
    private String token;
    // 设备类型
    private byte deviceType;

    private String sessionId;
}
