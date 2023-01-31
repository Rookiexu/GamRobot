package cn.rookiex.event.hero;

import cn.rookiex.message.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespHeroEquipGem implements RespGameEvent {
    @Override
    public int eventId() {
        return RespConstants.RespHeroEquipGem;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
    }
}
