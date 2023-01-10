package cn.rookiex.tree.node.composite;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.DefaultNode;
import cn.rookiex.tree.node.IsNode;
import cn.rookiex.tree.node.Node;

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
