package cn.rookiex.manager;

import cn.rookiex.robot.Robot;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author rookieX 2022/12/5
 */
@Log4j2
public class RobotManager {

    private final RobotConfig config = new RobotConfig();

    private final Map<Integer, ExecutorService> executorMap = Maps.newHashMap();

    private AtomicLong idCounter;

    private Map<Integer, Robot> robotMap = Maps.newHashMap();


    public void init() {
        this.idCounter = new AtomicLong();
        this.config.init();

        int threadCount = config.getThreadCount();
        for (int i = 0; i < threadCount; i++) {
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            executorMap.put(i, singleThreadExecutor);
        }

        log.info("加载压测机器人完成,当前执行线程数 : " + executorMap.size());

        log.info(this.config);
    }

    public void run(){
        int threadCount = config.getThreadCount();
        int robotCount = config.getRobotCount();
        for (int i = 0; i < robotCount; i++) {
            Robot robot = new Robot();
            try {
                robot.initChannel(config.getServerIp(), config.getServerPort());
                robot.setId(idCounter.addAndGet(1L));
                robot.setSimpleName(config.getRobotName());
                robot.setExecutorId(i % threadCount);
            } catch (Exception e) {
                log.info(e, e);
                System.exit(-1);
            }
        }
    }
}
