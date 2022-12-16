package cn.rookiex.event.hero;

import cn.rookiex.core.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespHeroLevelUp implements RespGameEvent {
    @Override
    public int eventId() {
        return RespConstants.RespHeroLevelUp;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
    }
}
