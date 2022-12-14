package cn.rookiex.event.login;

import cn.rookiex.core.Message;
import cn.rookiex.event.RespBase;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespSkipMain implements RespGameEvent {
    @Override
    public int eventId() {
        return 0;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespBase.dealResp0(message, robotContext);
    }
}
