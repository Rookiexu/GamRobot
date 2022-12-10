package cn.rookiex.event.login;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.RobotContext;

/**
 * 登录事件
 *
 * @author rookieX 2022/12/6
 */
public class ReqSkipMain implements ReqGameEvent {

    @Override
    public int eventId() {
        return 0;
    }

    @Override
    public void dealReq(RobotContext robotContext) {

    }
}
