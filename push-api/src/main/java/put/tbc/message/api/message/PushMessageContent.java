package put.tbc.message.api.message;

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
    private MsgIdentity from;
    private MsgIdentity to;
    private Object contentBody;
}
