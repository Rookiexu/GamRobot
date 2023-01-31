package cn.rookiex.robot.ctx;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.Robot;

/**
 * @author rookieX 2022/12/7
 */
public class RobotCtx implements RobotContext, RobotAiContext {

    private RobotManager robotManager;

    private Robot robot;

    private boolean skip = false;

    private ReqGameEvent aiEvent;

    private int aiSkip;

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
        return aiSkip > 100;
    }

    @Override
    public void runOver() {
    }

    @Override
    public void incrSkipTimes() {
        aiSkip++;
    }

    @Override
    public int getRunTimes() {
        return 0;
    }

    @Override
    public ReqGameEvent getReqEvent() {
        return aiEvent;
    }

    @Override
    public void setReqEvent(ReqGameEvent event) {
        aiEvent = event;
    }

    @Override
    public void aiReset() {
        aiEvent = null;
        aiSkip = 0;
    }
}
