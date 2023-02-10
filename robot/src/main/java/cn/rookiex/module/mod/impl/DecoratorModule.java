package cn.rookiex.module.mod.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.*;
import cn.rookiex.module.mod.BaseModule;
import cn.rookiex.module.mod.Module;
import cn.rookiex.module.mod.impl.AITreeModuleImpl;
import cn.rookiex.module.mod.impl.OrderModuleImpl;
import cn.rookiex.module.mod.impl.PreModuleImpl;
import cn.rookiex.robot.ctx.RobotContext;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

/**
 * 实现全部模块功能的基本module,
 *
 * @author rookieX 2022/12/7
 */
@Log4j2
public class DecoratorModule implements Module {

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
    public int getSortType() {
        return baseModule.getSortType();
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
        String type = config.getString("moduleType");
        switch (type) {
            case Module.PRE_MOD:
                this.runModule = new PreModuleImpl();
                break;
            case Module.ORDER_MOD:
                this.runModule = new OrderModuleImpl();
                break;
            case Module.AI_MOD:
                this.runModule = new AITreeModuleImpl();
                break;
            default:
                throw new IllegalArgumentException("moduleType is err : " + type);
        }
        this.runModule.init(config, moduleManager);
    }

}
