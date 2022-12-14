package cn.rookiex;

import cn.rookiex.manager.RobotConfig;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.*;

import java.util.Map;

/**
 * @author rookieX 2022/12/6
 */
public class RobotServer {

    private RobotServer() {
    }

    private static final RobotServer robotServer = new RobotServer();

    public static RobotServer getInstance() {
        return robotServer;
    }


    private RobotManager robotManager;

    private RobotConfig robotConfig;

    public void init(){
        this.robotManager = new RobotManager();
        robotManager.initProcessor();
        robotManager.initModules();
        robotManager.initRobot(new DefaultRobotFactory());
    }

    public RobotManager getRobotManager() {
        return robotManager;
    }

    public void start(){
        Map<Integer, RobotProcessor> processorMap = this.robotManager.getProcessorMap();
        for (RobotProcessor value : processorMap.values()) {
            value.start();
        }
    }

    public static void main(String[] args) {
        RobotServer instance = RobotServer.getInstance();
        instance.init();
        instance.start();
    }
}
