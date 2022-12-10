package cn.rookiex.event;

import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/5
 */
public interface ReqGameEvent extends GameEvent {
    void dealReq(RobotContext robotContext);
}
