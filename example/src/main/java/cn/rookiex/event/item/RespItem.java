package cn.rookiex.event.item;

import cn.rookiex.core.Message;
import cn.rookiex.event.RespEvent;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class RespItem implements RespEvent {
    @Override
    public int eventId() {
        return 0;
    }

    @Override
    public void dealResp(Message message, RobotContext robotContext) {

    }
}
