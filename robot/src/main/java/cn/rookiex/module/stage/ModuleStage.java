package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.RobotContext;

/**
 * @author rookieX 2022/12/12
 */
public interface ModuleStage {

    void setStage(ModuleStage stage);

    boolean isStageOver(RobotContext robotContext);

    boolean isModOver(RobotContext robotContext);

    void initMod(RobotContext robotContext);

    ModuleStage nextStage(RobotContext robotContext);

    ReqGameEvent getEvent(RobotContext robotContext);
}
