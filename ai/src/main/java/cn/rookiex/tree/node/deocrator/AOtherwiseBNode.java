package cn.rookiex.tree.node.deocrator;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.TreeStates;
import cn.rookiex.tree.node.DefaultNode;
import cn.rookiex.tree.node.IsNode;
import cn.rookiex.tree.node.Node;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

@IsNode
public class AOtherwiseBNode extends DefaultNode {

    private ConditionNode isNode;
    private Node planANode;
    private Node planBNode;

    @Override
    public TreeStates execute(AIContext context) {
        return isNode.isTrue(context) ? planANode.execute(context) : planBNode.execute(context);
    }

    @Override
    public Node init(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) throws IllegalAccessException, InstantiationException {
        JSONObject planA = jsonObject.getJSONObject("planA");
        JSONObject planB = jsonObject.getJSONObject("planB");
        JSONObject is = jsonObject.getJSONObject("is");

        isNode = (ConditionNode) init0(is, stringClassMap);
        planANode = init0(planA, stringClassMap);
        planBNode = init0(planB, stringClassMap);

        return this;
    }

    public Node init0(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) throws IllegalAccessException, InstantiationException {
        String act = jsonObject.getString("act");
        return stringClassMap.get(act).newInstance().init(jsonObject, stringClassMap);
    }

}
