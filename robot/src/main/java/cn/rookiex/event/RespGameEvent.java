package cn.rookiex.event;

import cn.rookiex.message.Message;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/5
 */
public interface RespGameEvent extends GameEvent {

    void dealResp(Message message, RobotContext robotContext);
}
