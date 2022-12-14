package cn.rookiex.manager;

import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.observer.Observable;
import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import cn.rookiex.observer.Observer;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.RobotContext;
import cn.rookiex.robot.RobotFactory;
import cn.rookiex.robot.RobotProcessor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author rookieX 2022/12/5
 */
@Log4j2
@Getter
public class RobotManager implements Observable {

    private final RobotConfig config = new RobotConfig();

    private final Record record = new Record();

    private final  ModuleManager moduleManager = new ModuleManager();

    private final Map<Integer, RobotProcessor> processorMap = Maps.newHashMap();

    private AtomicLong idCounter;

    private final Map<Integer, Robot> robotMap = Maps.newHashMap();

    private final Set<Observer> observers = Sets.newConcurrentHashSet();

    private boolean stop;

    /**
     * 机器人执行驱动tick长度10ms => 每秒100次
     */
    private long tickStep = 10;


    public void initProcessor() {
        this.idCounter = new AtomicLong();
        this.config.init();

        int threadCount = config.getThreadCount();
        for (int i = 0; i < threadCount; i++) {
            RobotProcessor robotProcessor = new RobotProcessor();
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            robotProcessor.setId(i);
            robotProcessor.setExecutorService(singleThreadExecutor);
            robotProcessor.setRobotManager(this);
            robotProcessor.register(record);
            record.getProcessorRecordMap().put(i, new ProcessorRecord());
            processorMap.put(i, robotProcessor);
        }

        log.info("加载压测机器人完成,当前执行数 : " + processorMap.size());


        log.info(this.config);
    }

    public RobotProcessor getProcessor(int id){
        RobotProcessor robotProcessor = processorMap.get(id);
        if (robotProcessor == null){
            log.error("压测机器人执行线程不存在 : " + id, new Throwable());
        }
        return robotProcessor;
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

        Map<String, Object> event = Maps.newHashMap();
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
                robotInitModules(robot);

                event.put(ObservedParams.PROCESSOR_ID, processorId);
                notify(ObservedEvents.INCR_ROBOT, event);
            } catch (Exception e) {
                log.info(e, e);
                System.exit(-1);
            }
        }
    }

    public void robotInitModules(Robot robot){
        ModuleManager moduleManager = getModuleManager();
        List<Module> randomModule = moduleManager.getRandomModule();

        robot.setCurModStage(Module.PRE);
        robot.setCurModIdx(0);
        robot.setCurEventIdx(0);

        List<Module> modules = Lists.newArrayList(randomModule);
        Collections.shuffle(modules);
        robot.setRandomModules(modules);
    }

    public void initModules() {
        this.moduleManager.init();
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void remove(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notify(String message, Map<String, Object> infoMap) {
        for (Observer observer : observers) {
            try{
                observer.update(message, infoMap);
            }catch (Exception e){
                log.error(e, e);
            }
        }
    }
}
