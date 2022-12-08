package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;

/**
 * @author rookieX 2022/12/8
 */
public class MyRobotFactory implements RobotFactory {
    @Override
    public Robot newRobot(RobotManager manager) {
        return new Robot();
    }

    @Override
    public RobotContext newRobotContext(RobotManager manager) {

        return null;
    }
}
