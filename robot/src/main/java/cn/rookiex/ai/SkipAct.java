package cn.rookiex.ai;

import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.action.ActNode;
import cn.rookiex.event.ReqSkip;
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
        aiContext.setReqEvent(ReqSkip.EVENT);
        aiContext.incrSkipTimes();
        return TreeStates.IS_RUN;
    }
}
