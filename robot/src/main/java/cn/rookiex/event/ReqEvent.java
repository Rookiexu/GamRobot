package cn.rookiex.event;

import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/5
 */
public interface ReqEvent extends Event {
    void dealReq(RobotContext robotContext);
}
