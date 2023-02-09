# gameRobot
## 简介

gameRobot是一个主要用于游戏压测的机器人程序,并在example提供了示例,理论上也可以用于其他.除了可以完成按模块的一应一答消息处理,还提供基于行为树的ai支持,让压测可以更接近玩家操作,另外也提供了对消息响应监控,以及可以自定义扩展的监控项目.

example特性:
1. 根据消息号一应一答处理
2. 支持行为树配置ai方式测试
3. 同时支持String和Json等多种消息编解码方式
4. 提供了基于滑动时间窗口的,吞吐量,响应速度等参数监测支持


## 文档说明

1. 设计思考 :  [游戏压测分析](https://zhuanlan.zhihu.com/p/604501690)
2. 代码设计 :  todo
3. example :
    1. EchoServer :  [模拟echo服务器](example/src/main/java/cn/rookiex/EchoServer.java)
    2. RobotServer : [压测机器人启动](example/src/main/java/cn/rookiex/RobotServer.java)

## 问题反馈

欢迎提交issues

也欢迎联系邮箱774590465@qq.com

