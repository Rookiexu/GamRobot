package cn.rookiex.module;

import cn.rookiex.event.ReqGameEvent;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 次序相关执行模块
 *
 * @author rookieX 2022/12/6
 */
public interface OrderModule {

    void initOrderConfig(JSONObject config, ModuleManager moduleManager);

    /**
     * 有序事件,先于乱序事件完成,参与模块循环
     */
     List<ReqGameEvent> getInorderEvents();

    /**
     * 乱序事件
     */
     List<ReqGameEvent> getDisorderEvents();


}
