package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.*;
import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.node.Node;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 实现全部模块功能的基本module,
 * @author rookieX 2022/12/7
 */
public class BaseModule implements Module, PreModule, OrderModule, AITreeModule {

    private String name;

    private int runTimes;

    private int order;

    private final AITreeModule aiTreeModule = new AITreeModuleImpl();

    private final PreModule preModule = new PreModuleImpl();

    private final OrderModule orderModule = new OrderModuleImpl();

    @Override
    public void initAIConfig(JSONObject config, ModuleManager moduleManager) {
        aiTreeModule.initAIConfig(config, moduleManager);
    }

    @Override
    public Node getNode() {
        return aiTreeModule.getNode();
    }

    @Override
    public AIContext getContext() {
        return aiTreeModule.getContext();
    }

    @Override
    public void aiTreeRun() {
        aiTreeModule.aiTreeRun();
    }

    @Override
    public void initOrderConfig(JSONObject config, ModuleManager moduleManager) {
        orderModule.initOrderConfig(config, moduleManager);
    }

    @Override
    public List<ReqGameEvent> getInorderEvents() {
        return orderModule.getInorderEvents();
    }

    @Override
    public List<ReqGameEvent> getDisorderEvents() {
        return orderModule.getDisorderEvents();
    }

    @Override
    public void initPreConfig(JSONObject config, ModuleManager moduleManager) {
        preModule.initPreConfig(config, moduleManager);
    }

    @Override
    public List<ReqGameEvent> getPreEvents() {
        return preModule.getPreEvents();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public int getRunTimes() {
        return runTimes;
    }

    @Override
    public void incrRunTimes() {
        this.runTimes++;
    }

    @Override
    public ReqGameEvent getNextEvent() {

        //当前阶段跳到下阶段

        //当前模块,执行到下个模块

        return null;
    }

    @Override
    public void init(JSONObject config, ModuleManager moduleManager) {
        this.name = config.getString("name");
        Integer order = config.getInteger("order");
        if (order == null){
            this.order = Integer.MAX_VALUE;
        }else {
            this.order = order;
        }

        initAIConfig(config, moduleManager);
        initPreConfig(config, moduleManager);
        initOrderConfig(config, moduleManager);
    }
}
