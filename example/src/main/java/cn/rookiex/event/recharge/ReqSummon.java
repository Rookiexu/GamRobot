package cn.rookiex.event.recharge;

import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2023/1/16
 */
public class ReqSummon implements ReqGameEvent {
    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        StrMessage strMessage = new StrMessage(eventId(), "i am summon !!!!");
        robot.getChannel().writeAndFlush(strMessage);
    }

    @Override
    public int waitId() {
        return RespConstants.RespSummon;
    }

    @Override
    public int eventId() {
        return RespConstants.ReqSummon;
    }
}
