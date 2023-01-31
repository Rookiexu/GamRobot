package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/12
 */
public class RunModule implements ModuleStage {

    private ModuleStage stage = new PreStage();

    @Override
    public boolean isStageOver(RobotContext robotContext) {
        return stage.isStageOver(robotContext);
    }

    @Override
    public boolean isModOver(RobotContext robotContext) {
        return stage.isModOver(robotContext);
    }

    @Override
    public void initMod(RobotContext robotContext) {
        stage.initMod(robotContext);
    }

    @Override
    public void initStage(RobotContext robotContext) {
        stage.initStage(robotContext);
    }

    @Override
    public ModuleStage nextStage(RobotContext robotContext) {
        return stage.nextStage(robotContext);
    }

    @Override
    public void toNextMod(RobotContext robotContext) {
        stage.toNextMod(robotContext);
    }

    @Override
    public ReqGameEvent getEvent(RobotContext robotContext) {
        return stage.getEvent(robotContext);
    }
}
