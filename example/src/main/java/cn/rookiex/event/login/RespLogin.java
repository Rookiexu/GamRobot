package cn.rookiex.event.login;

import cn.rookiex.core.Message;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespLogin implements RespGameEvent {
    @Override
    public int eventId() {
        return 0;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        String message1 = message.getMessage(String.class);

    }
}
