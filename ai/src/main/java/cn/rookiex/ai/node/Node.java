package cn.rookiex.ai.node;


import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.TreeStates;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface Node {

    TreeStates execute(AIContext context);

    void addSonNode(Node son);

    List<Node> getSonNode();

    Node init(JSONObject jsonObject, Map<String, Class<Node>> stringClassMap) throws IllegalAccessException, InstantiationException;
}
