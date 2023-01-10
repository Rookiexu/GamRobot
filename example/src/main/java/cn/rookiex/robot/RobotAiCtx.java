package cn.rookiex.robot;

import cn.rookiex.event.ReqGameEvent;
import lombok.Data;

/**
 * @author rookieX 2023/1/5
 */
@Data
public class RobotAiCtx implements RobotAiContext {

    private Robot robot;

    private ReqGameEvent reqGameEvent;

    @Override
    public void setRobotContext(RobotContext ctx) {

    }

    @Override
    public Robot getRobotContext() {
        return null;
    }
}
