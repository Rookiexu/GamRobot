package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.RobotAiContext;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.tree.AIContext;

/**
 * @author rookieX 2023/1/5
 */
public class AiStage implements ModuleStage {

    private static final ModuleStage stage = new AiStage();

    private RobotAiContext robotAiContext;

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        return false;
    }

    @Override
    public boolean isModOver(RobotContext robotContext) {
        return false;
    }

    @Override
    public void initMod(RobotContext robotContext) {

    }

    @Override
    public void initStage(RobotContext robotContext) {
        RobotManager robotManager = robotContext.getRobotManager();
        RobotAiContext robotAiContext = robotManager.getRobotFactory().newRobotAiContext(robotManager);
        robotAiContext.setRobotContext(robotContext);
        this.robotAiContext = robotAiContext;
    }

    @Override
    public ModuleStage nextStage(RobotContext robotContext) {
        return stage;
    }

    @Override
    public void toNextMod(RobotContext robotContext) {

    }

    @Override
    public ReqGameEvent getEvent(RobotContext robotContext) {
        return stage.getEvent(robotContext);
    }
}
