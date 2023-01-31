package cn.rookiex.module.stage;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.ctx.RobotContext;

/**
 * @author rookieX 2022/12/12
 */
public interface ModuleStage {

    boolean isStageOver(RobotContext robotContext);

    boolean isModOver(RobotContext robotContext);

    void initMod(RobotContext robotContext);

    void initStage(RobotContext robotContext);

    ModuleStage nextStage(RobotContext robotContext);

    void toNextMod(RobotContext robotContext);

    ReqGameEvent getEvent(RobotContext robotContext);
}
