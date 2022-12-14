package cn.rookiex.event.login;

import cn.rookiex.coon.SimpleMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;

/**
 * 登录事件
 *
 * @author rookieX 2022/12/6
 */
public class ReqSkipMain implements ReqGameEvent {

    @Override
    public int eventId() {
        return 0;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();
        robot.getChannel().writeAndFlush("ReqLogin");

        SimpleMessage simpleMessage = new SimpleMessage(eventId(), robot.getFullName() + " skip main");
        robot.getChannel().writeAndFlush(simpleMessage);
    }

    @Override
    public int waitId() {
        return 0;
    }
}
