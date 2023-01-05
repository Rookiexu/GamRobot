package cn.rookiex.ai.summon;

import cn.rookiex.robot.RobotAiCtx;
import cn.rookiex.robot.Robot;
import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.ActNode;

/**
 * @author rookieX 2023/1/5
 */
public class RechargeAct extends ActNode {
    @Override
    public TreeStates execute0(AIContext context) {
        RobotAiCtx aiContext = (RobotAiCtx) context;

        Robot robot = aiContext.getRobot();

        robot.dealSendEvent();
        return null;
    }
}
