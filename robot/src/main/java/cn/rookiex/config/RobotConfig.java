package cn.rookiex.config;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;


/**
 * @author rookieX 2022/12/5
 */
@Data
@Log4j2
public class RobotConfig {

    /**
     * 服务器ip
     */
    private String serverIp = "127.0.0.1";

    /**
     * 服务器接口
     */
    private int serverPort = 8090;

    /**
     * 并发机器人数量
     */
    private int robotCount = 20;

    /**
     * 单个机器人循环次数
     */
    private int runTimes = 100;


    /**
     * 请求间隔
     */
    private int reqIntervalTime = 1000;

    /**
     * robotName
     */
    private String robotName = "robot";

    /**
     * 执行线程数
     */
    private int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    public void init() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("robot.properties"));
            RobotConfig.initProperties(properties);
        } catch (IOException | IllegalAccessException e) {
            log.error(e, e);
        }

    }

    private boolean checkNull(String name, String value) {
        if (value == null || value.isEmpty()) {
            log.error("property : " + name + " ,is null or empty");
            return false;
        } else {
            return true;
        }
    }

    private static void initProperties(Properties properties) throws IllegalAccessException {
        Field[] fields = RobotConfig.class.getFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            boolean annotationPresent = field.isAnnotationPresent(SkipLoad.class);
            if (!annotationPresent) {
                String name = field.getName();
                String property = properties.getProperty(name);
                if (property != null) {
                    field.set(null, objTypeChange(property, type));
                }
            }
        }
    }

    private static Object objTypeChange(String from, Class<?> parameterType) {
        String simpleName = parameterType.getSimpleName();
        switch (simpleName) {
            case "Integer":
            case "int":
                return Integer.parseInt(from);
            case "Boolean":
            case "boolean":
                return Boolean.parseBoolean(from);
            case "Long":
            case "long":
                return Long.parseLong(from);
            case "String":
                return from;
        }
        log.error("TAConfig 配置加载异常,存在不能加载类型 : " + from);
        System.exit(-1);
        return null;
    }
}