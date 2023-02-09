package cn.rookiex.event.hero;

import cn.rookiex.coon.message.SimpleMessage;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespConstants;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class ReqGem implements ReqGameEvent {

    @Override
    public int eventId() {
        return RespConstants.ReqItem;
    }

    @Override
    public void dealReq(RobotContext robotContext) {
        Robot robot = robotContext.getRobot();

        SimpleMessage simpleMessage = new SimpleMessage(eventId(), robot.getFullName() + " 获取道具 Gem");
        robot.getChannel().writeAndFlush(simpleMessage) ;
    }

    @Override
    public int waitId() {
        return RespConstants.RespItem;
    }
}
