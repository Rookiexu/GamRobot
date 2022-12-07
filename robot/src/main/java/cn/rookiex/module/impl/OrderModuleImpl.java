package cn.rookiex.module.impl;

import cn.rookiex.event.ReqEvent;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.OrderModule;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author rookieX 2022/12/7
 */
@Log4j2
public class OrderModuleImpl implements OrderModule {

    /**
     * 有序事件
     */
    private List<ReqEvent> inorderEvents = Lists.newArrayList();

    /**
     * 随机事件
     */
    private List<ReqEvent> disorderEvents = Lists.newArrayList();

    @Override
    public void initOrderConfig(JSONObject config, ModuleManager moduleManager) {
        String name = config.getString("name");

        JSONArray inorderEvents = config.getJSONArray("inorderEvents");
        if (inorderEvents != null) {
            List<String> strings = inorderEvents.toJavaList(String.class);
            for (String eventName : strings) {
                ReqEvent reqEvent = moduleManager.getReqEventMap().get(eventName);
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
                ReqEvent reqEvent = moduleManager.getReqEventMap().get(eventName);
                if (reqEvent == null){
                    log.error("module : " + name + " ,加载disorder event不存在 : " + eventName);
                }else {
                    this.disorderEvents.add(reqEvent);
                }
            }
        }
    }

    @Override
    public List<ReqEvent> getInorderEvents() {
        return inorderEvents;
    }

    @Override
    public List<ReqEvent> getDisorderEvents() {
        return disorderEvents;
    }
}
