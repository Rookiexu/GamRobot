package cn.rookiex.event;

import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/6
 */
public class ReqSkip implements ReqGameEvent,SkipEvent {

    public static final ReqSkip event = new ReqSkip();

    @Override
    public void dealReq(RobotContext robotContext) {

    }

    @Override
    public int waitId() {
        return 0;
    }

    @Override
    public int eventId() {
        return 0;
    }
}
