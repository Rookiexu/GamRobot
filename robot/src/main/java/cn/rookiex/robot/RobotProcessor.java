package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.observer.*;
import cn.rookiex.observer.observed.ObservedEvents;
import cn.rookiex.observer.observed.ObservedParams;
import cn.rookiex.record.Record;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RobotProcessor implements Runnable, Observable {

    private int id;

    private ExecutorService executorService;

    private final List<Robot> robotList = Lists.newArrayList();

    private RobotManager robotManager;

    private final Set<Observer> observers = Sets.newConcurrentHashSet();

    /**
     * 提供初始加载的基础信息,只能读,不能写
     */
    private Map<String, Object> baseEventMap;

    private UpdateEvent incrEvent = new UpdateEventImpl(ObservedEvents.INCR_COON);

    private UpdateEvent decrEvent = new UpdateEventImpl(ObservedEvents.DECR_COON);

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
                        notify(incrEvent);
                    } else {
                        robot.dealRespEvent();
                        robot.dealSendEvent();
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
        incrEvent.put(ObservedParams.PROCESSOR_ID, getId());
        decrEvent.put(ObservedParams.PROCESSOR_ID, getId());
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }

    public RobotManager getRobotManager() {
        return robotManager;
    }

    public Record getRobotRecord(){
        return robotManager.getRecord();
    }

    public void start() {
        this.getExecutorService().submit(this);
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
    public void notify(UpdateEvent message) {
        for (Observer observer : observers) {
            try{
                observer.update(message);
            }catch (Exception e){
                log.error(e,e);
            }
        }
    }
}
