package cn.rookiex.event.recharge;

import cn.rookiex.coon.SimpleMessage;
import cn.rookiex.core.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2023/1/16
 */
public class RespRecharge implements RespGameEvent {
    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
        Robot robot = robotContext.getRobot();
    }

    @Override
    public int eventId() {
        return RespConstants.RespRecharge;
    }
}
