## 通信协议
```
 * +----+----------+-----------+-----------+------------+
 * |字段 | Length   | sessionId |   type    | body       |
 * +----+----------+-----------+-----------+------------+
 * |长度 | int:4byte| long:8byte|  byte:1   | jsonString |
 * +----+----------+-----------+-----------+------------+
 * |方法 |writeInt()|writeLong()|writeByte()|writeBytes()|
 * +----+----------+-----------+-----------+------------+

```
1. 开头四字节，长度信息（不包含长度字段的长度，是后面所有信息的长度，即总长度-4）；
2. 后跟8字节的sessionId（预留字段）和单字节的消息类型信息；
3. 最后是消息体，先编码为json字符串，再用utf8编码为字节数组；
***
## 服务端(jdk1.8+)
1. 启动： 
```
// 使用默认本地IP及端口
java com.edusky.message.server.ServerBootstrap
// 不使用默认值，自己指定
java com.edusky.message.server.ServerBootstrap 192.168.1.1 7007
```
或者打成可执行jar包后：
```
java -jar push-server.jar
```


## 客户端(jdk1.7+)
使用示例：
``` 
    //初始化
    final PushClient client = new PushBuilder()
            .callback(System.out::println)
            .deviceType((byte) 2)
            .openId("edu-space-blueSky-message-" + Thread.currentThread().getName())
            .getClient();
    // 后台启动
    new Thread(()->client.connect("192.168.1.178", 7007)).start();
    
    // 构建消息
    PushMessage message = PushMessage.buildRequestEntity();
    PushMessageContent content = message.getBody();
    content.setFrom(client.getIdentity());
    content.setTo(
            MsgIdentity.builder()
                    .deviceType((byte) 2)
                    .openId("12345678990")
                    .build()
    );
    // 发消息
    client.sendMsg(message);

```

## 待完成功能
1. 远程及动态配置；
2. 连接校验机制；
    * 简单校验：已预留sessionId字段，认证时由服务端生成并保存sessionId，客户端发送心跳时携带校验
3. 发消息权限验证；
4. 通信加密；
5. 业务消息日志；
6. 群发消息API（多用户、单用户多设备）；
7. 性能及可靠性测试、优化；