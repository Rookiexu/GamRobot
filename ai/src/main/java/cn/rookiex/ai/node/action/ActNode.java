package cn.rookiex.ai.node.action;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.DefaultNode;

public abstract class ActNode extends DefaultNode {
    @Override
    public TreeStates execute(AIContext context) {
        return execute0(context);
    }

    public abstract TreeStates execute0(AIContext context);
}
