package cn.rookiex.module;

import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.node.Node;
import com.alibaba.fastjson.JSONObject;

/**
 *  行为树执行模块
 * @author rookieX 2022/12/6
 */
public interface AITreeModule {

    void initAIConfig(JSONObject config, ModuleManager moduleManager);

    Node getNode();

    AIContext getContext();

    void aiTreeRun();
}
