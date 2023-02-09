package cn.rookiex.event.hero;

import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class ReqHeroLevelUp implements ReqGameEvent {

    @Override
    public int eventId() {
        return RespConstants.ReqHeroLevelUp;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        StrMessage strMessage = new StrMessage(eventId(), robot.getFullName() + " 获取道具 hero");
        robot.getChannel().writeAndFlush(strMessage);
    }

    @Override
    public int waitId() {
        return RespConstants.RespHeroLevelUp;
    }
}
