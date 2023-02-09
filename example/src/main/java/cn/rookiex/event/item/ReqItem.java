package cn.rookiex.event.item;

import cn.rookiex.coon.message.StrMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class ReqItem implements ReqGameEvent {

    @Override
    public int eventId() {
        return RespConstants.ReqItem;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        StrMessage strMessage = new StrMessage(eventId(), robot.getFullName() + " 获取道具");
        robot.getChannel().writeAndFlush(strMessage);
    }

    @Override
    public int waitId() {
        return RespConstants.RespItem;
    }
}
