package cn.rookiex.ai.node.deocrator;


import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.DefaultNode;
import cn.rookiex.ai.node.Node;

import java.util.List;

/**
 * 判断是否符合条件,符合条件继续执行子节点,知道返回错误或者执行
 */
public abstract class ConditionNode extends DefaultNode {

    @Override
    public TreeStates execute(AIContext context) {
        if(isTrue(context)){
            List<Node> sonNode = getSonNode();
            for (Node node : sonNode) {
                TreeStates ecResult = node.execute(context);
                //如果子节点返回错误或者运行中,向父节点返回错误或运行中
                if (ecResult == TreeStates.IS_FALSE || ecResult == TreeStates.IS_RUN) {
                    return ecResult;
                }
            }
            return TreeStates.IS_TRUE;
        }else {
            return TreeStates.IS_FALSE;
        }
    }

    public abstract boolean isTrue(AIContext context);
}
