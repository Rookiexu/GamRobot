package cn.rookiex;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.robot.ExampleRobotFactory;

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

    public void init(){
        this.robotManager = new RobotManager();
        this.robotManager.init(new ExampleRobotFactory());
    }

    public RobotManager getRobotManager() {
        return robotManager;
    }

    public void start(){
        robotManager.start();
    }

    public static void main(String[] args) {
        RobotServer instance = RobotServer.getInstance();
        instance.init();
        instance.start();
    }
}
