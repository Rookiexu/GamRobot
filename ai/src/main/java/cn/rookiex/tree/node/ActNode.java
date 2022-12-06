package cn.rookiex.tree.node;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;

public abstract class ActNode extends DefaultNode {
    @Override
    public TreeStates execute(AIContext context) {
        return execute0(context);
    }

    public abstract TreeStates execute0(AIContext context);
}
