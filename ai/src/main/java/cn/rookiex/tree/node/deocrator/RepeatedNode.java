package cn.rookiex.tree.node.deocrator;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.DefaultNode;
import cn.rookiex.tree.node.IsNode;
import cn.rookiex.tree.node.Node;

import java.util.List;

@IsNode
public class RepeatedNode extends DefaultNode {

    private int times;

    private TreeStates state;


    @Override
    public TreeStates execute(AIContext context) {
        for (int i = 0; i < times; i++) {
            List<Node> sonNode = getSonNode();
            for (Node node : sonNode) {
                TreeStates ecResult = node.execute(context);
                // 如果子节点返回正确或者运行中,向父节点
                if (ecResult == TreeStates.IS_TRUE || ecResult == TreeStates.IS_RUN) {
                    return ecResult;
                }
            }
        }
        return null;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
