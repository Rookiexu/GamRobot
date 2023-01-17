package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.*;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.tree.AIContext;
import cn.rookiex.tree.node.Node;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

/**
 * 实现全部模块功能的基本module,
 *
 * @author rookieX 2022/12/7
 */
@Log4j2
public class DefaultModule implements Module{

    private final BaseModule baseModule = new BaseModule();

    private Module runModule;

    @Override
    public String toString() {
        return getName();
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
    public ReqGameEvent getNextEvent(RobotContext context) {
        return runModule.getNextEvent(context);
    }

    @Override
    public void initRunEvent(RobotContext context) {
        this.runModule.initRunEvent(context);
    }

    @Override
    public boolean isRunOut(RobotContext context) {
        return this.runModule.isRunOut(context);
    }

    @Override
    public void init(JSONObject config, ModuleManager moduleManager) {
        baseModule.init(config, moduleManager);
        Integer type = config.getInteger("type");
        switch (type) {
            case Module.PRE:
                this.runModule = new PreModuleImpl();
                break;
            case Module.RANDOM:
            case Module.ORDER:
                this.runModule = new OrderModuleImpl();
                break;
            case Module.AI:
                this.runModule = new AITreeModuleImpl();
                break;
            default:
                throw new IllegalArgumentException("type is err : " + type);
        }
        this.runModule.init(config, moduleManager);
    }

}
