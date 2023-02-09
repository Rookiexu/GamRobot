package cn.rookiex.manager;

import cn.hutool.cron.CronUtil;
import cn.rookiex.config.RobotConfig;
import cn.rookiex.module.Module;
import cn.rookiex.module.ModuleManager;
import cn.rookiex.sentinel.observer.*;
import cn.rookiex.sentinel.observer.observed.ObservedEvents;
import cn.rookiex.sentinel.observer.observed.ObservedParams;
import cn.rookiex.sentinel.record.Record;
import cn.rookiex.sentinel.record.RecordProcessor;
import cn.rookiex.sentinel.record.SlowMsgRecord;
import cn.rookiex.robot.Robot;
import cn.rookiex.robot.ctx.RobotContext;
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
public class RobotManager {

    private final RobotConfig config = new RobotConfig();

    private final  ModuleManager moduleManager = new ModuleManager();

    private final Map<Integer, RobotProcessor> processorMap = Maps.newHashMap();

    private RecordProcessor recordProcessor;

    private AtomicLong idCounter;

    private final Map<String, Robot> robotMap = Maps.newHashMap();

    private final Set<Observer> observers = Sets.newConcurrentHashSet();

    private boolean stop;

    /**
     * 机器人执行驱动tick长度10ms => 每秒100次
     */
    private long tickStep = 10;

    private RobotFactory robotFactory;


    public void initProcessor() {
        this.idCounter = new AtomicLong();

        int threadCount = config.getThreadCount();
        for (int i = 0; i < threadCount; i++) {
            RobotProcessor robotProcessor = new RobotProcessor();
            ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
            robotProcessor.setId(i);
            robotProcessor.setExecutorService(singleThreadExecutor);
            robotProcessor.setRobotManager(this);
            processorMap.put(i, robotProcessor);
        }

        log.info("加载压测机器人完成,当前执行数 : " + processorMap.size());
        this.recordProcessor = new RecordProcessor();
        this.recordProcessor.setRobotManager(this);

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
        this.robotFactory = robotFactory;
        int threadCount = getConfig().getThreadCount();
        int robotCount = config.getRobotCount();

        for (int i = 0; i < robotCount; i++) {
            try {
                Robot robot = robotFactory.newRobot(this);
                robot.setId(idCounter.addAndGet(1L) + "");
                robot.setSimpleName(config.getRobotName());
                int processorId = i % threadCount;
                robot.setExecutorId(processorId);
                robot.setChannelInitializer(robotFactory.getChannelInitializer(this));
                RobotProcessor robotProcessor = processorMap.get(processorId);
                robotProcessor.getRobotList().add(robot);

                RobotContext robotContext = robotFactory.newRobotContext(this);
                robotContext.setRobotManager(this);
                robotContext.setRobot(robot);
                robot.setRobotContext(robotContext);
                robotInitModules(robot);
                robotMap.put(robot.getId(), robot);

                UpdateEventImpl updateEvent = new UpdateEventImpl(ObservedEvents.INCR_ROBOT);
                updateEvent.put(ObservedParams.PROCESSOR_ID, processorId);
                updateEvent.put(ObservedParams.ROBOT_ID, robot.getFullName());
                recordProcessor.notify(updateEvent);
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
        this.moduleManager.init(this.config);
    }

    public void initRecord() {
        Record record = new Record();
        record.getProcessorIds().addAll(processorMap.keySet());
        //60个窗口,10秒长度
        record.initWindow(40, 30000);
        this.getRecordProcessor().register(record);

        SlowMsgRecord slowMsgRecord = new SlowMsgRecord();
        slowMsgRecord.initWindow(60, 10000);
        this.getRecordProcessor().register(slowMsgRecord);
    }

    public void start(){
        Map<Integer, RobotProcessor> processorMap = getProcessorMap();
        for (RobotProcessor value : processorMap.values()) {
            value.start();
        }
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        getRecordProcessor().start();
    }

    public void init(RobotFactory factory){
        this.config.init();
        this.initProcessor();
        this.initModules();
        this.initRobot(factory);
        this.initRecord();
    }
}
