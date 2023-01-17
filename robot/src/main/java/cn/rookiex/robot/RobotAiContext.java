package cn.rookiex.robot;

import cn.rookiex.ai.AIContext;

/**
 * @author rookieX 2023/1/5
 */
public interface RobotAiContext extends AIContext {

    boolean isOver();

    void runOver();

    void incrRunTimes();

    int getRunTimes();
}
