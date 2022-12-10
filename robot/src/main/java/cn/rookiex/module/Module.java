package cn.rookiex.module;

import cn.rookiex.event.ReqGameEvent;
import com.alibaba.fastjson.JSONObject;

/**
 * 执行模块,通过配置确认模块的执行方法,执行顺序(也可以乱序)
 * @author rookieX 2022/12/5
 */
public interface Module {

    void setName(String name);

    String getName();

    int getOrder();

    int getRunTimes();

    void incrRunTimes();

    ReqGameEvent getNextEvent();

    void init(JSONObject config, ModuleManager moduleManager);
}
