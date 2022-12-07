package cn.rookiex.module;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * 测试任务
 * @author rookieX 2022/12/6
 */
@Data
public class TestProject {

    private List<Module> preModules;

    private List<Module> inorderModules;

    private List<Module> disorderModules;

    public void init(JSONObject config){
        JSONArray preModules = config.getJSONArray("preModules");
        JSONArray inorderModules = config.getJSONArray("inorderModules");
        JSONArray disorderModules = config.getJSONArray("disorderModules");

        List<String> strings = preModules.toJavaList(String.class);

    }
}
