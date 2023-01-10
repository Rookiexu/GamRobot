package cn.rookiex.tree.node.action;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.DefaultNode;

public abstract class ActNode extends DefaultNode {
    @Override
    public TreeStates execute(AIContext context) {
        return execute0(context);
    }

    public abstract TreeStates execute0(AIContext context);
}
