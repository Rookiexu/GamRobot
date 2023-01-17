package cn.rookiex.event.recharge;

import cn.rookiex.coon.SimpleMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2023/1/16
 */
public class ReqRecharge implements ReqGameEvent {
    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        SimpleMessage simpleMessage = new SimpleMessage(eventId(), "i am Recharge !!!!");
        robot.getChannel().writeAndFlush(simpleMessage);
    }

    @Override
    public int waitId() {
        return RespConstants.RespRecharge;
    }

    @Override
    public int eventId() {
        return RespConstants.ReqRecharge;
    }
}
