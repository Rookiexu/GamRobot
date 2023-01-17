package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.robot.RobotContext;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

/**
 * @author rookieX 2023/1/17
 */
public class BaseModule implements Module {

    private String name;

    private int order;

    private Integer moduleType;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModuleType() {
        return moduleType;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void init(JSONObject config, ModuleManager moduleManager) {
        this.name = config.getString("name");
        Integer order = config.getInteger("order");
        if (order == null) {
            this.order = Integer.MAX_VALUE;
        } else {
            this.order = order;
        }
        Integer type = config.getInteger("type");
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        } else {
            this.moduleType = type;
        }
    }

    @SneakyThrows
    @Override
    public ReqGameEvent getNextEvent(RobotContext context) {
        throw new IllegalAccessException("BaseOrder 不能执行指定方法");
    }

    @SneakyThrows
    @Override
    public void initRunEvent(RobotContext context) {
        throw new IllegalAccessException("BaseOrder 不能执行指定方法");
    }

    @SneakyThrows
    @Override
    public boolean isRunOut(RobotContext context) {
        throw new IllegalAccessException("BaseOrder 不能执行指定方法");
    }
}