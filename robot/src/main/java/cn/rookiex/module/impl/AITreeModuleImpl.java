package cn.rookiex.module.impl;

import cn.rookiex.module.AITreeModule;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.node.Node;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author rookieX 2022/12/6
 */
public class AITreeModuleImpl implements AITreeModule {

    @Override
    public void initAIConfig(JSONObject config, ModuleManager moduleManager) {
        //todo
    }

    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public AIContext getContext() {
        return null;
    }

    @Override
    public void aiTreeRun() {

    }
}
