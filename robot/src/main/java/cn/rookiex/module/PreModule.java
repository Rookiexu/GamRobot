package cn.rookiex.module;

import cn.rookiex.event.ReqGameEvent;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 前置模块,执行一次,不参与模块循环
 *
 * @author rookieX 2022/12/6
 */
public interface PreModule extends Module {

    void initPreConfig(JSONObject config, ModuleManager moduleManager);

    /**
     * 前置事件,不参与模块循环
     */
    List<ReqGameEvent> getPreEvents();
}
