package cn.rookiex.ai.logic;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.action.ActNode;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotCtx;

/**
 * @author rookieX 2023/1/5
 */
@IsNode
public class SkipAct extends ActNode {
    public static final SkipAct ACT = new SkipAct();

    @Override
    public TreeStates execute0(AIContext context) {
        RobotCtx aiContext = (RobotCtx) context;

        Robot robot = aiContext.getRobot();


        return TreeStates.IS_RUN;
    }
}
