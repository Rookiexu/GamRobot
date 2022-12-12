package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;

/**
 * @author rookieX 2022/12/7
 */
public interface RobotContext {

    void setRobotManager(RobotManager robotManager);

    RobotManager getRobotManager();

    void setRobot(Robot robot);

    Robot getRobot();

    boolean isSkip();

    void skipResp();

    void resetSkip();
}
