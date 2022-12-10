package cn.rookiex.module.impl;

import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.module.PreModule;
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
}
