package cn.rookiex.ai.logic.summon;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.action.ActNode;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotCtx;

import java.util.Map;

/**
 * @author rookieX 2023/1/5
 */
@IsNode
public class SummonAct extends ActNode {
    @Override
    public TreeStates execute0(AIContext context) {
        RobotCtx aiContext = (RobotCtx) context;
        Map<String, ReqGameEvent> reqEventMap = ((RobotCtx) context).getRobotManager().getModuleManager().getReqEventMap();
        aiContext.setReqEvent(reqEventMap.get("ReqSummon"));
        return TreeStates.IS_RUN;
    }
}
