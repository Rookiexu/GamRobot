package cn.rookiex.ai.summon;

import cn.rookiex.ai.node.IsNode;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotCtx;
import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.action.ActNode;

/**
 * @author rookieX 2023/1/5
 */
@IsNode
public class RechargeAct extends ActNode {
    @Override
    public TreeStates execute0(AIContext context) {
        RobotCtx aiContext = (RobotCtx) context;

        Robot robot = aiContext.getRobot();


        return TreeStates.IS_RUN;
    }
}
