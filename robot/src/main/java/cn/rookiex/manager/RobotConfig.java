package cn.rookiex.manager;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
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
            String serverIp = properties.getProperty("serverIp");
            if (checkNull("serverIp", serverIp)){
                this.serverIp = serverIp;
            }

            String serverPort = properties.getProperty("serverPort");
            if (checkNull("serverPort", serverPort)){
                this.serverPort = Integer.parseInt(serverPort);
            }

            String robotCount = properties.getProperty("robotCount");
            if (checkNull("robotCount", robotCount)){
                this.robotCount = Integer.parseInt(robotCount);
            }

            String runTimes = properties.getProperty("runTimes");
            if (checkNull("runTimes", runTimes)){
                this.runTimes = Integer.parseInt(runTimes);
            }

            String robotName = properties.getProperty("robotName");
            if (checkNull("robotName", robotName)){
                this.robotName = robotName;
            }

        } catch (IOException e) {
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
}