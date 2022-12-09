package cn.rookiex.module;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.rookiex.event.ReqEvent;
import cn.rookiex.event.RespEvent;
import cn.rookiex.module.impl.BaseModule;
import cn.rookiex.tree.node.IsNode;
import cn.rookiex.tree.node.Node;
import cn.rookiex.tree.util.PackageScanner;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 初始化加载Module和保管Module
 * @author rookieX 2022/12/6
 */
@Getter
@Log4j2
public class ModuleManager {

    /**
     * 模块map
     */
    private final Map<String, Module> moduleMap = Maps.newHashMap();

    /**
     * 请求事件map
     */
    private final Map<String, ReqEvent> reqEventMap = Maps.newHashMap();

    /**
     * 响应事件map
     */
    private final Map<Integer, RespEvent> respEventMap = Maps.newHashMap();

    public void init(){
        initEvents();
        initModules();
    }

    public void initModules() {
        log.info("加载压测模块开始,当前模块数量 : " + moduleMap.size());

        File modules = new File("modules");
        if (modules.isDirectory() && !FileUtil.isEmpty(modules)) {
            for (File file1 : modules.listFiles()) {
                String name = file1.getName();
                if (name.endsWith(".json")) {
                    FileReader fileReader = FileReader.create(file1);
                    String s = fileReader.readString();
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    //todo 后续可以优化为可扩展选择module实现类的方式
                    jsonObject.put("name", file1.getName().replace(".json",""));
                    BaseModule baseModule = new BaseModule();
                    baseModule.init(jsonObject, this);

                    if (moduleMap.containsKey(baseModule.getName())){
                        log.error("加载压测模块异常,存在多个同名module : " + baseModule.getName());
                    }
                    moduleMap.put(baseModule.getName(), baseModule);
                }
            }
        }else {
            log.error("加载压测模块异常,配置目录 modules 不存在,当前绝对路径: " + modules.getAbsolutePath());
        }
        log.info("加载压测模块完成,当前模块数量 : " + moduleMap.size());
    }

    public void initEvents(){
        log.info("加载压测事件开始,当前请求事件数量 : " + reqEventMap.size() + " ,当前响应事件数量 : " + respEventMap.size());
        Set<Class<?>> clazzs = PackageScanner.getClasses("cn.rookiex.event");
        Iterator<Class<?>> it = clazzs.iterator();
        Class<?> clazz = null;
        try {
            while (it.hasNext()) {
                clazz = it.next();
                Class<?>[] interfaces = clazz.getInterfaces();
                if (!clazz.isInterface()) {
                    for (Class<?> anInterface : interfaces) {
                        if (anInterface == ReqEvent.class) {
                            //请求可以重复,同一个消息可能在不同module有不同的处理
                            String simpleName = clazz.getSimpleName();
                            reqEventMap.put(simpleName, (ReqEvent) clazz.newInstance());
                        }

                        if (anInterface == RespEvent.class) {
                            //响应全局只有一个,可以分发逻辑,但是不能重复
                            RespEvent respEvent = (RespEvent) clazz.newInstance();
                            if (respEventMap.containsKey(respEvent.eventId())){
                                log.error("加载压测事件, 响应事件存在重复处理 : " + respEvent.eventId());
                            }
                            respEventMap.put(respEvent.eventId(), respEvent);
                        }
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        log.info("加载压测事件完成,当前请求事件数量 : " + reqEventMap.size() + " ,当前响应事件数量 : " + respEventMap.size());
    }

}
