package cn.rookiex.module.mod.impl;

import cn.rookiex.ai.RootNode;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.mod.AITreeModule;
import cn.rookiex.module.mod.BaseModule;
import cn.rookiex.module.mod.Module;
import cn.rookiex.robot.ctx.RobotAiContext;
import cn.rookiex.robot.ctx.RobotContext;
import cn.rookiex.ai.node.Node;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * @author rookieX 2022/12/6
 */
public class AITreeModuleImpl implements AITreeModule, Module {

    private Node node;

    private final BaseModule baseModule = new BaseModule();

    @SneakyThrows
    @Override
    public void initAIConfig(JSONObject config, ModuleManager moduleManager) {
        Map<String, Class<Node>> aiNodeMap = moduleManager.getAiNodeMap();
        JSONObject aiEvent = config.getJSONObject("aiEvent");
        String act = aiEvent.getString("act");
        Class<Node> nodeClass = aiNodeMap.get(act);
        Node node = nodeClass.newInstance();
        if (node instanceof RootNode) {
            node.init(aiEvent, aiNodeMap);
            this.node = node;
        }else {
            throw new IllegalAccessException("只能使用根节点作为起始节点");
        }
    }

    @Override
    public String getName() {
        return baseModule.getName();
    }

    @Override
    public int getSortType() {
        return baseModule.getSortType();
    }

    @Override
    public int getOrder() {
        return baseModule.getOrder();
    }

    @Override
    public void init(JSONObject config, ModuleManager moduleManager) {
        baseModule.init(config, moduleManager);
        initAIConfig(config, moduleManager);
    }

    @SneakyThrows
    @Override
    public ReqGameEvent getNextEvent(RobotContext context) {
        if (!(context instanceof RobotAiContext)) {
            throw new IllegalAccessException();
        }

        RobotAiContext aiContext = (RobotAiContext) context;
        node.execute(aiContext);
        aiContext.incrSkipTimes();
        return aiContext.getReqEvent();
    }

    @SneakyThrows
    @Override
    public void initRunEvent(RobotContext context) {
        if (!(context instanceof RobotAiContext)) {
            throw new IllegalAccessException();
        }

        RobotAiContext aiContext = (RobotAiContext) context;
        aiContext.aiReset();
    }

    @SneakyThrows
    @Override
    public boolean isRunOut(RobotContext context) {
        if (!(context instanceof RobotAiContext)) {
            throw new IllegalAccessException();
        }

        return ((RobotAiContext) context).isOver();
    }
}
