package cn.rookiex.ai.node.composite;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.DefaultNode;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.Node;

import java.util.List;

@IsNode
public class SequenceNode extends DefaultNode {
    @Override
    public TreeStates execute(AIContext context) {
        List<Node> sonNode = getSonNode();
        for (Node node : sonNode) {
            TreeStates ecResult = node.execute(context);
            //如果子节点返回错误,向父节点返回错误,否则基础执行
            if (ecResult == TreeStates.IS_FALSE) {
                return ecResult;
            }
        }
        //如果全部返回正确,向父节点返回正确
        return TreeStates.IS_TRUE;
    }

}
