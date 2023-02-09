package cn.rookiex.event.login;

import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * 登录事件
 *
 * @author rookieX 2022/12/6
 */
public class ReqSkipMain implements ReqGameEvent {

    @Override
    public int eventId() {
        return RespConstants.ReqSkipMain;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        StrMessage strMessage = new StrMessage(eventId(), robot.getFullName() + " skip main");
        robot.getChannel().writeAndFlush(strMessage);
    }

    @Override
    public int waitId() {
        return RespConstants.RespSkipMain;
    }
}
