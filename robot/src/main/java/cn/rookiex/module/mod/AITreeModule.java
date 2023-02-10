package cn.rookiex.module.mod;

import cn.rookiex.module.ModuleManager;
import com.alibaba.fastjson.JSONObject;

/**
 * 行为树执行模块
 *
 * @author rookieX 2022/12/6
 */
public interface AITreeModule extends Module {

    void initAIConfig(JSONObject config, ModuleManager moduleManager) throws IllegalAccessException, InstantiationException;

}
