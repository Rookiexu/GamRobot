package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.tree.AIContext;

/**
 * @author rookieX 2023/1/5
 */
public class AiStage implements ModuleStage {

    private static final ModuleStage stage = new AiStage();

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
        return null;
    }
}
