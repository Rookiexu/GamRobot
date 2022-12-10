package cn.rookiex.robot;

import cn.rookiex.manager.RobotManager;
import cn.rookiex.manager.Record;
import cn.rookiex.observer.Observable;
import cn.rookiex.observer.ObservedEvents;
import cn.rookiex.observer.ObservedParams;
import cn.rookiex.observer.Observer;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private long nextTick;

    @Override
    public void run() {
        initEventMap();
        nextTick = System.currentTimeMillis() + robotManager.getTickStep();
        while (!robotManager.isStop()) {
            for (Robot robot : robotList) {
                try {
                    if (!robot.isConnect()) {
                        robot.connect();
                        notify(ObservedEvents.INCR_COON, baseEventMap);
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

    private void initEventMap() {
        Map<String,Object> eventMap = Maps.newHashMap();
        eventMap.put(ObservedParams.PROCESSOR_ID, getId());

        baseEventMap = Collections.unmodifiableMap(eventMap);
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
    public void notify(String message, Map<String, Object> infoMap) {
        for (Observer observer : observers) {
            try{
                observer.update(message, infoMap);
            }catch (Exception e){
                log.error(e,e);
            }
        }
    }
}
