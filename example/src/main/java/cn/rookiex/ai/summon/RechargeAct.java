package cn.rookiex.ai.summon;

import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotCtx;
import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.action.ActNode;

/**
 * @author rookieX 2023/1/5
 */
public class RechargeAct extends ActNode {
    @Override
    public TreeStates execute0(AIContext context) {
        RobotCtx aiContext = (RobotCtx) context;

        Robot robot = aiContext.getRobot();

        return TreeStates.IS_RUN;
    }
}
