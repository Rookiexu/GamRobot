package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;

/**
 * @author rookieX 2022/12/7
 */
public class DefaultRobotContext implements RobotContext {

    private RobotManager robotManager;

    private Robot robot;

    @Override
    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }

    @Override
    public RobotManager getRobotManager() {
        return robotManager;
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }
}
