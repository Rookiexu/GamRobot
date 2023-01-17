package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.AITreeModule;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.ai.AIContext;
import cn.rookiex.ai.node.Node;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author rookieX 2022/12/6
 */
public class AITreeModuleImpl implements AITreeModule, Module {

    private final BaseModule baseModule = new BaseModule();

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

    @Override
    public String getName() {
        return baseModule.getName();
    }

    @Override
    public int getModuleType() {
        return baseModule.getModuleType();
    }

    @Override
    public int getOrder() {
        return baseModule.getOrder();
    }

    @Override
    public void init(JSONObject config, ModuleManager moduleManager) {
        baseModule.init(config, moduleManager);
    }

    @Override
    public ReqGameEvent getNextEvent(RobotContext context) {
        return null;
    }

    @Override
    public void initRunEvent(RobotContext context) {

    }

    @Override
    public boolean isRunOut(RobotContext context) {
        return false;
    }
}
