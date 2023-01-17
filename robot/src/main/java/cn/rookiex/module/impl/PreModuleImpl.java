package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.PreModule;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/7
 */
@Log4j2
public class PreModuleImpl implements PreModule {

    private final BaseModule baseModule = new BaseModule();

    private List<ReqGameEvent> preEvents = Lists.newArrayList();

    @Override
    public void initPreConfig(JSONObject config, ModuleManager moduleManager) {
        String name = config.getString("name");

        JSONArray inorderEvents = config.getJSONArray("preEvents");
        if (inorderEvents != null) {
            List<String> strings = inorderEvents.toJavaList(String.class);
            for (String eventName : strings) {
                ReqGameEvent reqEvent = moduleManager.getReqEventMap().get(eventName);
                if (reqEvent == null){
                    log.error("module : " + name + " ,加载pre event不存在 : " + eventName);
                }else {
                    this.preEvents.add(reqEvent);
                }
            }
        }
    }

    @Override
    public List<ReqGameEvent> getPreEvents() {
        return preEvents;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (ReqGameEvent preEvent : preEvents) {
            builder.append(preEvent.getClass().getSimpleName()).append(",");
        }
        builder.append("]");

        return "PreModuleImpl{" +
                "preEvents=" + builder +
                '}';
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
        initPreConfig(config, moduleManager);
    }

    @Override
    public ReqGameEvent getNextEvent(RobotContext context) {
        Robot robot = context.getRobot();
        int eventIdx = robot.getCurEventIdx();
        List<ReqGameEvent> curEventList = robot.getCurEventList();
        if (curEventList.size() > eventIdx) {
            ReqGameEvent reqGameEvent = curEventList.get(eventIdx);
            robot.setCurEventIdx(eventIdx + 1);
            return reqGameEvent;
        } else {
            log.error("模组 " + getName() + " ,已完成event执行,需要执行下个模组");
        }
        return null;
    }

    @Override
    public void initRunEvent(RobotContext context) {
        Robot robot = context.getRobot();
        int modRunTimes = robot.getModRunTimes();
        List<ReqGameEvent> modEvents = Lists.newArrayList();

        //运行次数作为是否执行预处理事件可以优化
        if (modRunTimes == 0) {
            List<ReqGameEvent> preEvents = getPreEvents();
            modEvents.addAll(preEvents);
        }

        robot.setModEventList(modEvents);
    }

    @Override
    public boolean isRunOut(RobotContext context) {
        Robot robot = context.getRobot();
        int eventIdx = robot.getCurEventIdx();
        List<ReqGameEvent> curEventList = robot.getCurEventList();
        return curEventList == null || curEventList.size() <= eventIdx;
    }
}
