package cn.rookiex.sentinel.record;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.sentinel.observer.*;
import cn.rookiex.sentinel.observer.observed.ObservedEvents;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.AbstractQueue;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rookieX 2022/12/8
 */
@Getter
@Log4j2
public class RecordProcessor implements Runnable, Observable {

    private final List<Observer> observers = Lists.newCopyOnWriteArrayList();

    private final UpdateEvent event = new UpdateEventImpl(ObservedEvents.TICK_TIME);

    private final AbstractQueue<UpdateEvent> eventQue = Queues.newConcurrentLinkedQueue();

    private RobotManager robotManager;

    @Override
    public void run() {
        int i = 0;
        log.info("记录线程开始执行");
        while (!robotManager.isStop()){
            try {
                UpdateEvent poll = eventQue.poll();
                if (poll != null) {
                    i++;
                    for (Observer observer : observers) {
                        observer.update(poll);
                    }
                }
            }catch (Exception e){
                log.error(e,e);
            }
        }
        log.info("记录线程执行结束退出");
    }

    public void tickLog(){
        for (Observer observer : observers) {
            if (observer instanceof TickLog){
                ((TickLog) observer).logInfo();
            }
        }
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
        eventQue.offer(message);
    }

    public void start() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(this);
        CronUtil.schedule("0 0/1 * * * ?", (Task) this::tickLog);
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }
}
