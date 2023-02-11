# 游戏服务器模拟

在正式实现压测脚本前,

## 模拟游戏服务器

GameRobot本身提供了可供运行的example例子,但只是机器人客户端其实并不能完成测试,所以GameRobot内置了一个简单的Echo服务器,虽然简单,但是包含了游戏服务器在通信层面的基本功能,具体特性如下:

1. 实现自定义的传输协议
2. 支持不同类型的解析格式
3. 模拟一定的服务器延迟

## 插入式协议编解码

message接口

DataCodec接口

选择stringcodec实现simpleMessage

MsgDecoder:取出四字节长度的消息id,以及剩下的消息体,放入new出来的simpleMessage对象中

MsgEncoder:通过message接口获得消息字节组,将4字节总长度,4字节消息号,消息数据写到netty输出ByteBuf中

zaiEchoServer中只是实现了对不同解析方式的支持,并没有实现同时支持这些结构的消息,如果想要同时实现不同的,其实也是可以的

1. 添加一个消息处理handler

## 自定义传输协议

### 最简单的协议设计

在example的EchoServer中,将协议的二进制数据流约定为4个字节的消息长度字段,4个字节的消息码字段,以及剩下的消息内容字段

整个GameRobot基于Java语言实现,所以EchoServer自然选择了Netty,在协议分包这一块,可以选择自己定义

netty提供了LengthFieldBasedFrameDecoder帮助处理消息分隔问题,让我们可以避免处理半包,粘包等问题

    创建一个最大长度512*1024字节,4位长度,不包含4字节长度位的数据的分解handler
    new LengthFieldBasedFrameDecoder(1024 * 512, 0, 4, 0, 4)
    
    tips:在不同的语言中,高低字节的顺序可能是不一样的,比如c#和java就不一样,在解析长度的时候可能需要设置高低位

### 可供扩展的方案

添加需要的参数:加密信息,解析类型,版本号

快速支持

```log
INFO  | cn.rookiex.module.ModuleManager.sortModules(ModuleManager.java:125) : 加载压测模块顺序,前置模块数量 : 1 , : [login]
INFO  | cn.rookiex.module.ModuleManager.sortModules(ModuleManager.java:126) : 加载压测模块顺序,顺序模块数量 : 1 , : [multi]
INFO  | cn.rookiex.module.ModuleManager.sortModules(ModuleManager.java:127) : 加载压测模块顺序,随机模块数量 : 0 , : []
INFO  | cn.rookiex.sentinel.record.RecordProcessor.run(RecordProcessor.java:35) : 记录线程开始执行
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:49) : robot_1 receive str msg : 1 , data : i am login !!!!
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:49) : robot_1 receive str msg : 4 , data : robot_1 获取道具
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:57) : robot_1 deal json msg : 4 , data : {"msg":"robot_1 获取json道具","name":"robot_1"}
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:57) : robot_1 deal json msg : 4 , data : {"msg":"robot_1 获取json道具","name":"robot_1"}
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:49) : robot_1 receive str msg : 4 , data : robot_1 获取道具
INFO  | cn.rookiex.event.RespConstants.dealResp0(RespConstants.java:49) : robot_1 receive str msg : 4 , data : robot_1 获取道具
```


### 业务处理

正常的游戏服务器,会对消息id进行路由,分法消息到对应的业务和线程执行消息响应,但是gameRobot只是模拟,所以将客户端上传消息解析后反向传回客户端即可

#### 服务器延迟

为了模拟服务器的延迟,使用延时任务执行客户端消息返回,在延迟任务的选择上,netty自带一个时间轮延时器,

