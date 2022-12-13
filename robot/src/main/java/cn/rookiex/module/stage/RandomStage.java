package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/12
 */
public class RandomStage implements ModuleStage {

    @Override
    public void setStage(ModuleStage stage) {

    }

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
    public ModuleStage nextStage(RobotContext robotContext) {
        return null;
    }

    @Override
    public ReqGameEvent getEvent(RobotContext robotContext) {
        return null;
    }
}
