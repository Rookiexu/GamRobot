package cn.rookiex.module;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ClassUtil;
import cn.rookiex.event.ReqGameEvent;
import cn.rookiex.event.RespGameEvent;
import cn.rookiex.module.impl.DefaultModule;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.*;

/**
 * 初始化加载Module和保管Module
 *
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
    private final Map<String, ReqGameEvent> reqEventMap = Maps.newHashMap();

    /**
     * 响应事件map
     */
    private final Map<Integer, RespGameEvent> respEventMap = Maps.newHashMap();

    /**
     * 前置模块,适用于登录加载等事件
     */
    private List<Module> preModule;

    /**
     * 顺序模块,适用于英雄模块,需要前置于英雄装备等情况
     */
    private List<Module> orderModule;

    /**
     * 随机模块,不限制执行顺序模块,获得,社团,好友等等
     */
    private List<Module> randomModule;

    public void init() {
        initEvents();
        initModules();
    }

    public void initModules() {
        loadModules();
        sortModules();
    }

    private void sortModules() {
        log.info("加载压测模块顺序开始");
        this.preModule = Lists.newArrayList();
        this.orderModule = Lists.newArrayList();
        this.randomModule = Lists.newArrayList();
        for (Module value : moduleMap.values()) {
            int type = value.getModuleType();
            switch (type) {
                case Module.RANDOM:
                    randomModule.add(value);
                    break;
                case Module.ORDER:
                    orderModule.add(value);
                    break;
                case Module.PRE:
                    preModule.add(value);
                    break;
                default:
                    log.error(value.getName() + " ,模块执行类型不存在,不会执行对应模块 : "  + type);
            }
        }
        preModule.sort(Comparator.comparing(Module::getOrder));
        orderModule.sort(Comparator.comparing(Module::getOrder));

        log.info("加载压测模块顺序,前置模块数量 : " + preModule.size() + " , : " + preModule);
        log.info("加载压测模块顺序,顺序模块数量 : " + orderModule.size() + " , : " + orderModule.toString());
        log.info("加载压测模块顺序,随机模块数量 : " + randomModule.size() + " , : " + randomModule.toString());
    }

    private void loadModules() {
        log.info("加载压测模块开始");

        File modules = new File("modules");
        if (modules.isDirectory() && !FileUtil.isEmpty(modules)) {
            for (File file1 : modules.listFiles()) {
                String name = file1.getName();
                if (name.endsWith(".json")) {
                    FileReader fileReader = FileReader.create(file1);
                    String s = fileReader.readString();
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    //todo 后续可以优化为可扩展选择module实现类的方式
                    jsonObject.put("name", file1.getName().replace(".json", ""));
                    DefaultModule defaultModule = new DefaultModule();
                    defaultModule.init(jsonObject, this);

                    if (moduleMap.containsKey(defaultModule.getName())) {
                        log.error("加载压测模块异常,存在多个同名module : " + defaultModule.getName());
                    }
                    moduleMap.put(defaultModule.getName(), defaultModule);
                }
            }
        } else {
            log.error("加载压测模块异常,配置目录 modules 不存在,当前绝对路径: " + modules.getAbsolutePath());
        }
        log.info("加载压测模块完成,当前模块数量 : " + moduleMap.size());
    }

    public void initEvents() {
        log.info("加载压测事件开始");
        Set<Class<?>> clazzs = ClassUtil.scanPackage("cn.rookiex.event");
        Iterator<Class<?>> it = clazzs.iterator();
        Class<?> clazz = null;
        try {
            while (it.hasNext()) {
                clazz = it.next();
                Class<?>[] interfaces = clazz.getInterfaces();
                if (!clazz.isInterface()) {
                    for (Class<?> anInterface : interfaces) {
                        if (anInterface == ReqGameEvent.class) {
                            //请求可以重复,同一个消息可能在不同module有不同的处理
                            String simpleName = clazz.getSimpleName();
                            reqEventMap.put(simpleName, (ReqGameEvent) clazz.newInstance());
                        }

                        if (anInterface == RespGameEvent.class) {
                            //响应全局只有一个,可以分发逻辑,但是不能重复
                            RespGameEvent respEvent = (RespGameEvent) clazz.newInstance();
                            if (respEventMap.containsKey(respEvent.eventId())) {
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
