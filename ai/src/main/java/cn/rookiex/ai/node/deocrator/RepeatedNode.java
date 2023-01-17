package cn.rookiex.ai.node.deocrator;

import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import cn.rookiex.ai.node.DefaultNode;
import cn.rookiex.ai.node.IsNode;
import cn.rookiex.ai.node.Node;

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
