package cn.rookiex.record;

import cn.hutool.Hutool;
import cn.hutool.cron.CronUtil;
import cn.rookiex.manager.RobotManager;
import cn.rookiex.observer.*;
import cn.rookiex.observer.observed.ObservedEvents;
import cn.rookiex.observer.observed.ObservedParams;
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
        while (!robotManager.isStop()){
            UpdateEvent poll = eventQue.poll();
            if (poll != null){
                for (Observer observer : observers) {
                    observer.update(poll);
                }
            }else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        eventQue.add(message);
    }

    public void start() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(this);
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }
}
