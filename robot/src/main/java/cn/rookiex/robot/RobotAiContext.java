package cn.rookiex.robot;

import cn.rookiex.ai.AIContext;
import cn.rookiex.event.ReqGameEvent;

/**
 * @author rookieX 2023/1/5
 */
public interface RobotAiContext extends AIContext {

    boolean isOver();

    void runOver();

    void incrRunTimes();

    int getRunTimes();

    ReqGameEvent getReqEvent();

    void reset();
}
