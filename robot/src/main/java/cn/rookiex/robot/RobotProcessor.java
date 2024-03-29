package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.sentinel.pubsub.*;
import cn.rookiex.sentinel.pubsub.cons.SystemEventsKeys;
import cn.rookiex.sentinel.pubsub.cons.SystemEventParams;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RobotProcessor implements Runnable {

    private int id;

    private ExecutorService executorService;

    private final List<Robot> robotList = Lists.newArrayList();

    private RobotManager robotManager;

    /**
     * 提供初始加载的基础信息,只能读,不能写
     */
    private Map<String, Object> baseEventMap;

    private SystemEvent incrEvent = new SystemEventImpl(SystemEventsKeys.INCR_COON);

    private SystemEvent decrEvent = new SystemEventImpl(SystemEventsKeys.DECR_COON);

    public void setId(int id) {
        this.id = id;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private long nextTick;

    @Override
    public void run() {
        initInfoMap();
        nextTick = System.currentTimeMillis() + robotManager.getTickStep();
        while (!robotManager.isStop()) {
            for (Robot robot : robotList) {
                try {
                    if (!robot.isConnect()) {
                        robot.connect();
                        robotManager.getRecordProcessor().publish(incrEvent);
                    } else {
                        if (robot.isReady()) {
                            robot.dealRespEvent();
                            robot.dealSendEvent();
                        }
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
            try {
                long l = nextTick - System.currentTimeMillis();
                if (l > 0) {
                    Thread.sleep(l);
                }
                nextTick += robotManager.getTickStep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initInfoMap() {
        incrEvent.put(SystemEventParams.PROCESSOR_ID, getId());
        decrEvent.put(SystemEventParams.PROCESSOR_ID, getId());
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
