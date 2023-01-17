package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.OrderModule;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

/**
 * @author rookieX 2022/12/7
 */
@Log4j2
public class OrderModuleImpl implements OrderModule {

    private final BaseModule baseModule = new BaseModule();

    /**
     * 有序事件
     */
    private List<ReqGameEvent> inorderEvents = Lists.newArrayList();

    /**
     * 随机事件
     */
    private List<ReqGameEvent> disorderEvents = Lists.newArrayList();

    @Override
    public void initOrderConfig(JSONObject config, ModuleManager moduleManager) {
        String name = config.getString("name");

        JSONArray inorderEvents = config.getJSONArray("inorderEvents");
        if (inorderEvents != null) {
            List<String> strings = inorderEvents.toJavaList(String.class);
            for (String eventName : strings) {
                ReqGameEvent reqEvent = moduleManager.getReqEventMap().get(eventName);
                if (reqEvent == null){
                    log.error("module : " + name + " ,加载inorder event不存在 : " + eventName);
                }else {
                    this.inorderEvents.add(reqEvent);
                }
            }
        }

        JSONArray disorderEvents = config.getJSONArray("disorderEvents");
        if (disorderEvents != null) {
            List<String> strings = disorderEvents.toJavaList(String.class);
            for (String eventName : strings) {
                ReqGameEvent reqEvent = moduleManager.getReqEventMap().get(eventName);
                if (reqEvent == null){
                    log.error("module : " + name + " ,加载disorder event不存在 : " + eventName);
                }else {
                    this.disorderEvents.add(reqEvent);
                }
            }
        }
    }

    @Override
    public List<ReqGameEvent> getInorderEvents() {
        return inorderEvents;
    }

    @Override
    public List<ReqGameEvent> getDisorderEvents() {
        return disorderEvents;
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
        initOrderConfig(config, moduleManager);
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
        List<ReqGameEvent> modEvents = Lists.newArrayList();

        modEvents.addAll(getInorderEvents());

        List<ReqGameEvent> disorderEvents = getDisorderEvents();
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
}
