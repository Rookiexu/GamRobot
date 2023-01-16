package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;

/**
 * @author rookieX 2022/12/7
 */
public class RobotCtx implements RobotContext,RobotAiContext {

    private RobotManager robotManager;

    private Robot robot;

    private boolean skip = false;

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

    @Override
    public boolean isSkip() {
        return skip;
    }

    @Override
    public void skipResp() {
        skip = true;
    }

    @Override
    public void resetSkip() {
        this.skip = false;
    }

    @Override
    public boolean isOver() {
        return false;
    }

    @Override
    public void runOver() {

    }

    @Override
    public void incrRunTimes() {

    }

    @Override
    public int getRunTimes() {
        return 0;
    }
}
