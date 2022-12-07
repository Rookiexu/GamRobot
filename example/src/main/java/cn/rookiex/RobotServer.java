package cn.rookiex;

import cn.rookiex.manager.RobotConfig;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.module.ModuleManager;

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

    private ModuleManager moduleManager;

    private RobotManager robotManager;

    private RobotConfig robotConfig;

    public void init(){
        RobotManager robotManager = new RobotManager();
        robotManager.init();
        this.robotManager = robotManager;

        ModuleManager moduleManager = new ModuleManager();
        moduleManager.init();
        this.moduleManager = moduleManager;
    }

    public void run(){
        this.robotManager.run();
    }

    public static void main(String[] args) {
        RobotServer instance = RobotServer.getInstance();
        instance.init();
    }
}
