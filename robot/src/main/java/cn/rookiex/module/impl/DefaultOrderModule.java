package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.*;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

/**
 * 实现全部模块功能的基本module,
 * @author rookieX 2022/12/7
 */
@Log4j2
public class DefaultOrderModule implements Module, PreModule, OrderModule{

    private String name;

    private int runTimes;

    private int order;

    private int type;

//    private final AITreeModule aiTreeModule = new AITreeModuleImpl();

    private final PreModule preModule = new PreModuleImpl();

    private final OrderModule orderModule = new OrderModuleImpl();

//    @Override
//    public void initAIConfig(JSONObject config, ModuleManager moduleManager) {
//        aiTreeModule.initAIConfig(config, moduleManager);
//    }
//
//    @Override
//    public Node getNode() {
//        return aiTreeModule.getNode();
//    }
//
//    @Override
//    public AIContext getContext() {
//        return aiTreeModule.getContext();
//    }
//
//    @Override
//    public void aiTreeRun() {
//        aiTreeModule.aiTreeRun();
//    }

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
    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return type;
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
    public ReqGameEvent getNextEvent(RobotContext context) {
        Robot robot = context.getRobot();
        int eventIdx = robot.getCurEventIdx();
        List<ReqGameEvent> curEventList = robot.getCurEventList();
        if (curEventList.size() > eventIdx){
            ReqGameEvent reqGameEvent = curEventList.get(eventIdx);
            robot.setCurEventIdx(eventIdx + 1);
            return reqGameEvent;
        }else {
            log.error("模组 "+ getName() + " ,已完成event执行,需要执行下个模组");
        }
        return null;
    }

    @Override
    public void initRunEvent(RobotContext context) {
        Robot robot = context.getRobot();
        int modRunTimes = robot.getModRunTimes();
        List<ReqGameEvent> modEvents = Lists.newArrayList();
        if (modRunTimes == 0){
            List<ReqGameEvent> preEvents = preModule.getPreEvents();
            modEvents.addAll(preEvents);
        }
        modEvents.addAll(orderModule.getInorderEvents());

        List<ReqGameEvent> disorderEvents = orderModule.getDisorderEvents();
        List<ReqGameEvent> randomList = Lists.newArrayList(disorderEvents);
        Collections.shuffle(randomList);
        modEvents.addAll(randomList);

        robot.setModEventList(modEvents);
    }

    @Override
    public boolean isRunOut(RobotContext context) {
        Robot robot = context.getRobot();
        int eventIdx = robot.getCurEventIdx();
        List<ReqGameEvent> curEventList = robot.getCurEventList();
        return curEventList == null || curEventList.size() <= eventIdx;
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
        Integer type = config.getInteger("type");
        if (type == null){
            this.type = Module.RANDOM;
        }else {
            this.type = type;
        }

//        initAIConfig(config, moduleManager);
        initPreConfig(config, moduleManager);
        initOrderConfig(config, moduleManager);
    }
}
