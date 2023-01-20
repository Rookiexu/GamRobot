package cn.rookiex.event.login;

import cn.rookiex.message.Message;
import cn.rookiex.event.RespConstants;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespSkipMain implements RespGameEvent {
    @Override
    public int eventId() {
        return RespConstants.RespSkipMain;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {
        RespConstants.dealResp0(message, robotContext);
    }
}
