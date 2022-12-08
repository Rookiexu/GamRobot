package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author rookieX 2022/12/8
 */
@Getter
public class RobotProcessor implements Runnable {

    private ExecutorService executorService;

    private final List<Robot> robotList = Lists.newArrayList();

    private RobotManager robotManager;

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private long nextTick;

    @Override
    public void run() {
        nextTick = System.currentTimeMillis() + robotManager.getTickStep();
        while (!robotManager.isStop()) {
            for (Robot robot : robotList) {
                robot.dealRespEvent();
                robot.dealSendEvent();
            }
            try {
                long l = nextTick - System.currentTimeMillis();
                if (l > 0){
                    Thread.sleep(l);
                }
                nextTick += robotManager.getTickStep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }

    public RobotManager getRobotManager() {
        return robotManager;
    }

    public void start() {
        this.getExecutorService().submit(this);
    }
}
