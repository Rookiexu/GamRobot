package cn.rookiex.robot.ctx;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;

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
