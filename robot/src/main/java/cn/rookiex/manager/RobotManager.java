package cn.rookiex.manager;

import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.robot.RobotFactory;
import cn.rookiex.robot.RobotProcessor;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author rookieX 2022/12/5
 */
@Log4j2
@Getter
public class RobotManager {

    private final RobotConfig config = new RobotConfig();

    private final Map<Integer, RobotProcessor> processorMap = Maps.newHashMap();

    private AtomicLong idCounter;

    private Map<Integer, Robot> robotMap = Maps.newHashMap();

    private boolean stop;

    /**
     * 机器人执行驱动tick长度10ms => 每秒100次
     */
    private long tickStep = 100;


    public void initProcessor() {
        this.idCounter = new AtomicLong();
        this.config.init();

        int threadCount = config.getThreadCount();
        for (int i = 0; i < threadCount; i++) {
            RobotProcessor robotProcessor = new RobotProcessor();
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            robotProcessor.setExecutorService(singleThreadExecutor);
            robotProcessor.setRobotManager(this);
            processorMap.put(i, robotProcessor);
        }

        log.info("加载压测机器人完成,当前执行数 : " + processorMap.size());


        log.info(this.config);
    }

    public void run(){

    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public long getTickStep() {
        return tickStep;
    }

    public void setTickStep(long tickStep) {
        this.tickStep = tickStep;
    }

    public void initRobot(RobotFactory robotFactory) {
        int threadCount = getConfig().getThreadCount();
        int robotCount = config.getRobotCount();
        for (int i = 0; i < robotCount; i++) {
            try {
                Robot robot = robotFactory.newRobot(this);
                robot.setId(idCounter.addAndGet(1L));
                robot.setSimpleName(config.getRobotName());
                int processorId = i % threadCount;
                robot.setExecutorId(processorId);
                RobotProcessor robotProcessor = processorMap.get(processorId);
                robotProcessor.getRobotList().add(robot);

                RobotContext robotContext = robotFactory.newRobotContext(this);
                robotContext.setRobotManager(this);
                robotContext.setRobot(robot);
                robot.setRobotContext(robotContext);
            } catch (Exception e) {
                log.info(e, e);
                System.exit(-1);
            }
        }
    }
}
